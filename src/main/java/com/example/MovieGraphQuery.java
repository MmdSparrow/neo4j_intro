// import org.neo4j.driver.*;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Session;




public class MovieGraphQuery {
    public static void main(String[] args) {
        // Database connection details
        String uri = "bolt://localhost:7687"; // Adjust URI if using a remote instance
        String user = "neo4j"; // Replace with your username
        String password = "password"; // Replace with your password

        // Create a driver instance
        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))) {
            // Open a session
            try (Session session = driver.session()) {
                // Cypher query to retrieve movies and actors
                String query = """
                    MATCH (actor:Person)-[:ACTED_IN]->(movie:Movie)
                    RETURN movie.title AS movie, actor.name AS actor
                    LIMIT 10
                """;

                // Execute the query and print results
                session.readTransaction(tx -> {
                    var result = tx.run(query);
                    while (result.hasNext()) {
                        var record = result.next();
                        System.out.println("Movie: " + record.get("movie").asString() +
                                           ", Actor: " + record.get("actor").asString());
                    }
                    return null;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
