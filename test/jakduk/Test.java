package jakduk;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 16.
 * @desc     :
 */
public class Test {
    private static int fetchSize = 1000;
    private static String url = "http://localhost:8983/solr/core1/";
    private static HttpSolrServer solrCore;

    public Test() throws MalformedURLException
    {
         solrCore = new HttpSolrServer(url);
    }

    /**
     * Takes an SQL ResultSet and adds the documents to solr. Does it in batches
     * of fetchSize.
     * 
     * @param rs
     *            A ResultSet from the database.
     * @return The number of documents added to solr.
     * @throws SQLException
     * @throws SolrServerException
     * @throws IOException
     */
    public long addResultSet(ResultSet rs) throws SQLException,
            SolrServerException, IOException
    {
        long count = 0;
        int innerCount = 0;
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        ResultSetMetaData rsm = rs.getMetaData();
        int numColumns = rsm.getColumnCount();
        String[] colNames = new String[numColumns + 1];

        /**
         * JDBC numbers the columns starting at 1, so the normal java convention
         * of starting at zero won't work.
         */
        for (int i = 1; i < (numColumns + 1); i++)
        {
            colNames[i] = rsm.getColumnName(i);
            /**
             * If there are fields that you want to handle manually, check for
             * them here and change that entry in colNames to null. This will
             * cause the loop in the next section to skip that database column.
             */
            // //Example:
            // if (rsm.getColumnName(i) == "db_id")
            // {
            // colNames[i] = null;
            // }
        }

        while (rs.next())
        {
            count++;
            innerCount++;

            SolrInputDocument doc = new SolrInputDocument();

            /**
             * At this point, take care of manual document field assignments for
             * which you previously assigned the colNames entry to null.
             */
            // //Example:
            // doc.addField("solr_db_id", rs.getLong("db_id"));

            for (int j = 1; j < (numColumns + 1); j++)
            {
                if (colNames[j] != null)
                {
                    Object f;
                    switch (rsm.getColumnType(j))
                    {
                        case Types.BIGINT:
                        {
                            f = rs.getLong(j);
                            break;
                        }
                        case Types.INTEGER:
                        {
                            f = rs.getInt(j);
                            break;
                        }
                        case Types.DATE:
                        {
                            f = rs.getDate(j);
                            break;
                        }
                        case Types.FLOAT:
                        {
                            f = rs.getFloat(j);
                            break;
                        }
                        case Types.DOUBLE:
                        {
                            f = rs.getDouble(j);
                            break;
                        }
                        case Types.TIME:
                        {
                            f = rs.getDate(j);
                            break;
                        }
                        case Types.BOOLEAN:
                        {
                            f = rs.getBoolean(j);
                            break;
                        }
                        default:
                        {
                            f = rs.getString(j);
                        }
                    }
                    doc.addField(colNames[j], f);
                }
            }
            docs.add(doc);

            /**
             * When we reach fetchSize, index the documents and reset the inner
             * counter.
             */
            if (innerCount == fetchSize)
            {
                solrCore.add(docs);
                docs.clear();
                innerCount = 0;
            }
        }

        /**
         * If the outer loop ended before the inner loop reset, index the
         * remaining documents.
         */
        if (innerCount != 0)
        {
            solrCore.add(docs);
        }
        return count;
    }
}
