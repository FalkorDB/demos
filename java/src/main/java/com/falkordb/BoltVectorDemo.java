package com.falkordb;

import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;


public class BoltVectorDemo {
	public static void main(String args[]) {
		try (Driver driver = GraphDatabase.driver("bolt://localhost:7687")) {
			try (var session = driver.session()) {
				
				// Create Vector index on field description in Character
				session.run("CREATE VECTOR INDEX FOR (c:Character) ON (c.description) OPTIONS {dim:5, similarityFunction:'euclidean'}");
				
				// Fill in the Graph with some data on books and characters
				session.run("CREATE "
						+ "(:Character {name:'Bastian Balthazar Bux', description:vector32f([0.1, 0.3, 0.3, 0.4, 0.7])})-[:in]->(book1:Book {name:'The Neverending Story'}), "
						+ "(:Character {name:'Atreyu', description:vector32f([0.3, 0.6, 0.2, 0.1, 0.4])})-[:in]->(book1), "
						+ "(:Character {name:'Jareth', description:vector32f([0.1, 0.3, 0.1, 0.2, 0.9])})-[:in]->(book2:Book {name:'Labyrinth'}), "
						+ "(:Character {name:'Hoggle', description:vector32f([0.3, 0.2, 0.5, 0.7, 0.9])})-[:in]->(book2)");
				
				// Find the book with the character description that is most similar to the user's query
				Result result = session.run("CALL db.idx.vector.query("
						+ "{type:'NODE', label:'Character', attribute:'description', query:vector32f([0.1, 0.4, 0.3, 0.2, 0.7]), k:1}) "
						+ "YIELD entity "
						+ "MATCH (c:Character)-[]->(b:Book) WHERE c = entity "
						+ "RETURN b.name AS name");
				
				 while (result.hasNext()) {
					Record record = result.next();
				    System.out.println(record.get("name"));
				 }
			}

		}
	}
}