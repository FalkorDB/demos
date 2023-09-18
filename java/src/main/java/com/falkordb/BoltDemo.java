package com.falkordb;

import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;


public class BoltDemo {
	public static void main(String args[]) {
		try (Driver driver = GraphDatabase.driver("bolt://localhost:7687")) {
			try (var session = driver.session()) {

				session.run("CREATE "
						+ "(:Rider {name:'Valentino Rossi'})-[:rides]->(:Team {name:'Yamaha'}),"
						+ " (:Rider {name:'Dani Pedrosa'})-[:rides]->(:Team {name:'Honda'}),"
						+ " (:Rider {name:'Andrea Dovizioso'})-[:rides]->(:Team {name:'Ducati'})");
				
				session.run("CREATE INDEX FOR (t:Rider) ON (t.name)");
				session.run("CREATE INDEX FOR (t:Team) ON (t.name)");
			
				Result result = session.run("MATCH (r:Rider)-[:rides]->(t:Team) "
						+ "WHERE t.name = 'Yamaha' RETURN r.name, t.name");
				
				 while (result.hasNext()) {
					Record record = result.next();
				    System.out.println(record.get("r.name").asString() 
				    		+ " in " + record.get("t.name").asString());
				 }
			}

		}
	}
}