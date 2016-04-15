package com.jakduk.configuration.mongo;

import com.mongodb.WriteConcern;
import net.exacode.spring.social.connect.SocialConnectionDao;
import net.exacode.spring.social.connect.mongo.MongoConnection;
import net.exacode.spring.social.connect.mongo.MongoConnectionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * A MongoDB data access object for spring social connections
 * 
 * @author mendlik
 */
@Service
public class MongoConnectionDao implements SocialConnectionDao {

	private final MongoTemplate mongoTemplate;
	private final MongoConnectionConverter converter;

	@Autowired
	public MongoConnectionDao(MongoTemplate mongoTemplate,
			MongoConnectionConverter converter) {
		this.mongoTemplate = mongoTemplate;
		this.converter = converter;
	}

	/**
	 * Returns the max connection rank for the user and the provider.
	 * 
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#getMaxRank(String,
	 *      String)
	 */
	@Override
	public int getMaxRank(String userId, String providerId) {
		// select coalesce(max(rank) + 1, 1) as rank from UserConnection where
		// userId = ? and providerId = ?
		Query q = query(where("userId").is(userId).and("providerId")
				.is(providerId));

		q.with(new Sort(Direction.DESC, "rank"));
		MongoConnection cnn = mongoTemplate.findOne(q, MongoConnection.class);

		if (cnn == null)
			return 1;

		return cnn.getRank() + 1;
	}

	/**
	 * Create a new connection for the user.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#create(String,
	 *      org.springframework.social.connect.Connection, int)
	 */
	@Override
	public void create(String userId, Connection<?> userConn, int rank) {
		MongoConnection mongoCnn = converter.convert(userConn);
		mongoCnn.setUserId(userId);
		mongoCnn.setRank(rank);
		mongoTemplate.insert(mongoCnn);
	}

	/**
	 * Update a connection.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#update(String,
	 *      org.springframework.social.connect.Connection)
	 */
	@Override
	public void update(String userId, Connection<?> userConn) {
		MongoConnection mongoCnn = converter.convert(userConn);
		mongoCnn.setUserId(userId);
		try {
			mongoTemplate.setWriteConcern(WriteConcern.SAFE);
			mongoTemplate.save(mongoCnn);
		} catch (DuplicateKeyException e) {
			Query q = query(where("userId").is(userId).and("providerId")
					.is(mongoCnn.getProviderId()).and("providerUserId")
					.is(mongoCnn.getProviderUserId()));

			Update update = Update
					.update("expireTime", mongoCnn.getExpireTime())
					.set("accessToken", mongoCnn.getAccessToken())
					.set("profileUrl", mongoCnn.getProfileUrl())
					.set("imageUrl", mongoCnn.getImageUrl())
					.set("displayName", mongoCnn.getDisplayName());

			mongoTemplate.findAndModify(q, update, MongoConnection.class);
		}
	}

	/**
	 * Remove a connection.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#remove(String,
	 *      org.springframework.social.connect.ConnectionKey)
	 */
	@Override
	public void remove(String userId, ConnectionKey connectionKey) {
		// delete where userId = ? and providerId = ? and providerUserId = ?
		Query q = query(where("userId").is(userId).and("providerId")
				.is(connectionKey.getProviderId()).and("providerUserId")
				.is(connectionKey.getProviderUserId()));
		mongoTemplate.remove(q, MongoConnection.class);
	}

	/**
	 * Remove all the connections for a user on a provider.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#remove(String,
	 *      String)
	 */
	@Override
	public void remove(String userId, String providerId) {
		// delete where userId = ? and providerId = ?
		Query q = query(where("userId").is(userId).and("providerId")
				.is(providerId));

		mongoTemplate.remove(q, MongoConnection.class);
	}

	/**
	 * Return the primary connection.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#getPrimaryConnection(String,
	 *      String)
	 */
	@Override
	public Connection<?> getPrimaryConnection(String userId, String providerId) {
		// where userId = ? and providerId = ? and rank = 1
		Query q = query(where("userId").is(userId).and("providerId")
				.is(providerId).and("rank").is(1));

		MongoConnection mc = mongoTemplate.findOne(q, MongoConnection.class);
		return converter.convert(mc);
	}

	/**
	 * Get the connection for user, provider and provider user id.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#getConnection(String,
	 *      String, String)
	 */
	@Override
	public Connection<?> getConnection(String userId, String providerId,
			String providerUserId) {
		// where userId = ? and providerId = ? and providerUserId = ?
		Query q = query(where("userId").is(userId).and("providerId")
				.is(providerId).and("providerUserId").is(providerUserId));

		MongoConnection mc = mongoTemplate.findOne(q, MongoConnection.class);
		return converter.convert(mc);
	}

	/**
	 * Get all the connections for an user id.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#getConnections(String)
	 */
	@Override
	public List<Connection<?>> getConnections(String userId) {
		// select where userId = ? order by providerId, rank
		Query q = query(where("userId").is(userId));
		q.with(new Sort(Direction.ASC, "providerId"));
		q.with(new Sort(Direction.ASC, "rank"));
		return runQuery(q);
	}

	/**
	 * Get all the connections for an user id on a provider.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#getConnections(String,
	 *      String)
	 */
	@Override
	public List<Connection<?>> getConnections(String userId, String providerId) {
		// where userId = ? and providerId = ? order by rank
		Query q = new Query(where("userId").is(userId).and("providerId")
				.is(providerId));
		q.with(new Sort(Direction.ASC, "rank"));

		return runQuery(q);
	}

	/**
	 * Get all the connections for an user.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#getConnections(String,
	 *      org.springframework.util.MultiValueMap)
	 */
	@Override
	public List<Connection<?>> getConnections(String userId,
			MultiValueMap<String, String> providerUsers) {
		// userId? and providerId = ? and providerUserId in (?, ?, ...) order by
		// providerId, rank

		if (providerUsers == null || providerUsers.isEmpty()) {
			throw new IllegalArgumentException(
					"Unable to execute find: no providerUsers provided");
		}

		List<Criteria> lc = new ArrayList<Criteria>();
		for (Entry<String, List<String>> entry : providerUsers.entrySet()) {
			String providerId = entry.getKey();

			lc.add(where("providerId").is(providerId).and("providerUserId")
					.in(entry.getValue()));
		}

		Criteria criteria = where("userId").is(userId);
		criteria.orOperator(lc.toArray(new Criteria[lc.size()]));

		Query q = new Query(criteria);
		q.with(new Sort(Direction.ASC, "providerId"));
		q.with(new Sort(Direction.ASC, "rank"));

		return runQuery(q);
	}

	/**
	 * Get the user ids on the provider.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#getUserIds(String,
	 *      Set)
	 */
	@Override
	public Set<String> getUserIds(String providerId, Set<String> providerUserIds) {
		// select userId from " + tablePrefix + "UserConnection where providerId
		// = :providerId and providerUserId in (:providerUserIds)
		Query q = query(where("providerId").is(providerId)
				.and("providerUserId")
				.in(new ArrayList<String>(providerUserIds)));
		q.fields().include("userId");

		List<MongoConnection> results = mongoTemplate.find(q,
				MongoConnection.class);
		Set<String> userIds = new HashSet<String>();
		for (MongoConnection mc : results) {
			userIds.add(mc.getUserId());
		}

		return userIds;
	}

	/**
	 * Get the user ids on the provider with a given provider user id.
	 *
	 * @see net.exacode.spring.social.connect.SocialConnectionDao#getUserIds(String,
	 *      String)
	 */
	@Override
	public List<String> getUserIds(String providerId, String providerUserId) {
		// select userId where providerId = ? and providerUserId = ?",
		Query q = query(where("providerId").is(providerId)
				.and("providerUserId").is(providerUserId));
		q.fields().include("userId");

		List<MongoConnection> results = mongoTemplate.find(q,
				MongoConnection.class);
		List<String> userIds = new ArrayList<String>();
		for (MongoConnection mc : results) {
			userIds.add(mc.getUserId());
		}

		return userIds;
	}

	private List<Connection<?>> runQuery(Query query) {
		List<MongoConnection> results = mongoTemplate.find(query,
				MongoConnection.class);
		List<Connection<?>> l = new ArrayList<Connection<?>>();
		for (MongoConnection mc : results) {
			l.add(converter.convert(mc));
		}

		return l;
	}

}