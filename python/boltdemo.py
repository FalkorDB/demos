from neo4j import GraphDatabase

URI = "neo4j://localhost:7687"

with GraphDatabase.driver(URI) as driver:

    driver.execute_query("CREATE VECTOR INDEX FOR (c:Character) ON (c.description) OPTIONS {dim:5, similarityFunction:'euclidean'}")

    driver.execute_query( """CREATE 
                         (:Character {name:'Bastian Balthazar Bux', description:vector32f([0.1, 0.3, 0.3, 0.4, 0.7])})-[:in]->(book1:Book {name:'The Neverending Story'}),
						 (:Character {name:'Atreyu', description:vector32f([0.3, 0.6, 0.2, 0.1, 0.4])})-[:in]->(book1),
						 (:Character {name:'Jareth', description:vector32f([0.1, 0.3, 0.1, 0.2, 0.9])})-[:in]->(book2:Book {name:'Labyrinth'}),
						 (:Character {name:'Hoggle', description:vector32f([0.3, 0.2, 0.5, 0.7, 0.9])})-[:in]->(book2)""")
    
    records, _, _ = driver.execute_query( """CALL db.idx.vector.query
                                         ({type:'NODE', label:'Character', attribute:'description', query:vector32f([0.1, 0.4, 0.3, 0.2, 0.7]), k:1}) 
                                         YIELD entity 
                                         MATCH (c:Character)-[]->(b:Book) WHERE c = entity 
                                         RETURN b.name AS name""")
    
    for record in records:
        print(record)