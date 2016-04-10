package com.jakduk.configuration.mongo;

import com.google.common.collect.ImmutableList;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.social.connect.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.transform;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

public class MongoConnectionRepository implements ConnectionRepository {

    private final String userId;
    private final MongoOperations mongo;
    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final MongoConnectionTransformers mongoConnectionTransformers;

    public MongoConnectionRepository(final String userId, final MongoOperations mongo, final ConnectionFactoryLocator connectionFactoryLocator, final MongoConnectionTransformers mongoConnectionTransformers) {
        this.userId = userId;
        this.mongo = mongo;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.mongoConnectionTransformers = mongoConnectionTransformers;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        final Query query = query(where("userId").is(userId)).with(sortByProviderId().and(sortByCreated()));
        final List<Connection<?>> results = findConnections(query);
        final MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<>();
        for (String registeredProviderId : connectionFactoryLocator.registeredProviderIds()) {
            connections.put(registeredProviderId, ImmutableList.<Connection<?>>of());
        }
        for (Connection<?> connection : results) {
            final String providerId = connection.getKey().getProviderId();
            if (connections.get(providerId).isEmpty()) {
                connections.put(providerId, new LinkedList<Connection<?>>());
            }
            connections.add(providerId, connection);
        }
        return connections;
    }

    @Override
    public List<Connection<?>> findConnections(final String providerId) {
        final Query query = query(where("userId").is(userId).and("providerId").is(providerId)).with(sortByCreated());
        return ImmutableList.copyOf(findConnections(query));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> List<Connection<A>> findConnections(final Class<A> apiType) {
        final List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(final MultiValueMap<String, String> providerUserIds) {
        if (providerUserIds == null || providerUserIds.isEmpty()) {
            throw new IllegalArgumentException("providerUserIds must be defined");
        }
        
        final List<Criteria> filters = new ArrayList<>(providerUserIds.size());
        for (Map.Entry<String, List<String>> entry : providerUserIds.entrySet()) {
            final String providerId = entry.getKey();
            filters.add(where("providerId").is(providerId).and("providerUserId").in(entry.getValue()));
        }

        final Criteria criteria = where("userId").is(userId);
        criteria.orOperator(filters.toArray(new Criteria[filters.size()]));

        final Query query = new Query(criteria).with(sortByProviderId().and(sortByCreated()));
        final List<Connection<?>> results = transform(mongo.find(query, MongoConnection.class), mongoConnectionTransformers.toConnection());

        MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<>();
        for (Connection<?> connection : results) {
            final String providerId = connection.getKey().getProviderId();
            final String providerUserId = connection.getKey().getProviderUserId();
            final List<String> userIds = providerUserIds.get(providerId);
            List<Connection<?>> connections = connectionsForUsers.get(providerId);
            if (connections == null) {
                connections = new ArrayList<>(userIds.size());
                for (int i = 0; i < userIds.size(); i++) {
                    connections.add(null);
                }
                connectionsForUsers.put(providerId, connections);
            }
            final int connectionIndex = userIds.indexOf(providerUserId);
            connections.set(connectionIndex, connection);
        }
        return connectionsForUsers;
    }

    @Override
    public Connection<?> getConnection(final ConnectionKey connectionKey) {
        final Query query = query(where("userId").is(userId).and("providerId").is(connectionKey.getProviderId()).and("providerUserId").is(connectionKey.getProviderUserId()));
        final Connection<?> connection = findOneConnection(query);
        if (connection == null) {
            throw new NoSuchConnectionException(connectionKey);
        } else {
            return connection;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getConnection(final Class<A> apiType, final String providerUserId) {
        final String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getPrimaryConnection(final Class<A> apiType) {
        final String providerId = getProviderId(apiType);
        final Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        } else {
            return connection;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> findPrimaryConnection(final Class<A> apiType) {
        final String providerId = getProviderId(apiType);
        return (Connection<A>) findPrimaryConnection(providerId);
    }

    @Override
    public void addConnection(final Connection<?> connection) {
        try {
            final MongoConnection mongoConnection = mongoConnectionTransformers.fromConnection(userId).apply(connection);
            mongo.insert(mongoConnection);
        } catch (DuplicateKeyException ex) {
            throw new DuplicateConnectionException(connection.getKey());
        }
    }

    @Override
    public void updateConnection(final Connection<?> connection) {
        final MongoConnection mongoConnection = mongoConnectionTransformers.fromConnection(userId).apply(connection);
        final Query query = query(where("userId").is(userId).and("providerId").is(mongoConnection.getProviderId()).and("providerUserId").is(mongoConnection.getProviderUserId()));
        final Update update =
                update("displayName", mongoConnection.getDisplayName())
                .set("profileUrl", mongoConnection.getProfileUrl())
                .set("imageUrl", mongoConnection.getImageUrl())
                .set("accessToken", mongoConnection.getAccessToken())
                .set("secret", mongoConnection.getSecret())
                .set("refreshToken", mongoConnection.getRefreshToken())
                .set("expireTime", mongoConnection.getExpireTime());
        mongo.updateFirst(query, update, MongoConnection.class);
    }

    @Override
    public void removeConnections(final String providerId) {
        final Query query = query(where("userId").is(userId).and("providerId").is(providerId));
        mongo.remove(query, MongoConnection.class);
    }

    @Override
    public void removeConnection(final ConnectionKey connectionKey) {
        final Query query = query(where("userId").is(userId).and("providerId").is(connectionKey.getProviderId()).and("providerUserId").is(connectionKey.getProviderUserId()));
        mongo.remove(query, MongoConnection.class);
    }

    private Connection<?> findPrimaryConnection(String providerId) {
        final Query query = query(where("userId").is(userId).and("providerId").is(providerId)).with(sortByCreated());
        return findOneConnection(query);
    }

    private List<Connection<?>> findConnections(Query query) {
        return transform(mongo.find(query, MongoConnection.class), mongoConnectionTransformers.toConnection());
    }
    
    private Connection<?> findOneConnection(Query query) {
        return mongoConnectionTransformers.toConnection().apply(mongo.findOne(query, MongoConnection.class));
    }

    private <A> String getProviderId(Class<A> apiType) {
        return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
    }
    
    private Sort sortByProviderId() {
        return new Sort(Sort.Direction.ASC, "providerId");
    }
    
    private Sort sortByCreated() {
        return new Sort(Sort.Direction.DESC, "created");
    }
}
