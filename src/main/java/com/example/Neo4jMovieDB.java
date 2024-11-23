package com.example;
import java.util.List;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

public class Neo4jMovieDB {
    public static void main(String[] args) {
        String uri = "bolt://localhost:7687";
        String username = "neo4j";
        String password = "password";

        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
             Session session = driver.session()) {

            String cypherQuery = """
                    MATCH (p:Person)-[:ACTED_IN]->(m:Movie)
                    WHERE p.name = "Tom Hanks"
                    LIMIT 25
                    RETURN COUNT(m) AS totalMovies, COLLECT(m.title) AS movieTitles;
            """;
            Result result = session.run(cypherQuery);

            while (result.hasNext()) {
                Record record = result.next();
                int totalMovies = record.get("totalMovies").asInt();
                List<String> movieTitles = record.get("movieTitles").asList(Value::asString);

                System.out.println("Total Movies: " + totalMovies);
                System.out.println("Movie Titles: " + movieTitles);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
