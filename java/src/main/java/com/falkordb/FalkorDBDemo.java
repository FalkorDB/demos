package com.falkordb;

import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.graph.ResultSet;
import redis.clients.jedis.graph.Record;

public class FalkorDBDemo {
	public static void main(String args[]) {
		try (UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379")) {

			jedis.graphQuery("MotoGP", "CREATE "
					+ "(:Rider {name:'Valentino Rossi'})-[:rides]->(:Team {name:'Yamaha'}), "
					+ "(:Rider {name:'Dani Pedrosa'})-[:rides]->(:Team {name:'Honda'}), "
					+ "(:Rider {name:'Andrea Dovizioso'})-[:rides]->(:Team {name:'Ducati'})");
			
			jedis.graphQuery("MotoGP", "CREATE INDEX FOR (r:Rider) ON (r.name)");
			jedis.graphQuery("MotoGP", "CREATE INDEX FOR (t:Team) ON (t.name)");
			

			ResultSet result = jedis.graphQuery("MotoGP", "MATCH (r:Rider)-[:rides]->(t:Team) "
					+ "WHERE t.name = 'Yamaha' RETURN r.name, t.name");
			
			 for (Record record : result) {
			    System.out.println(record.getString("r.name") 
			    		+ " in " + record.getString("t.name"));
			 }
		}
	}
}