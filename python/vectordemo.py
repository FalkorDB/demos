from falkordb import FalkorDB

# Connect to FalkorDB
db = FalkorDB(host='localhost', port=6379)
graph = db.select_graph('Books')

# Create Vector index on field description in Character
graph.create_node_vector_index('Character', 'description', dim=5, similarity_function='euclidean')

# Fill in the Graph with some data on books and characters
graph.query("""CREATE 
            (:Character {name:'Bastian Balthazar Bux', description:vecf32([0.1, 0.3, 0.3, 0.4, 0.7])})-[:in]->(book1:Book {name:'The Neverending Story'}),
            (:Character {name:'Atreyu', description:vecf32([0.3, 0.6, 0.2, 0.1, 0.4])})-[:in]->(book1),
            (:Character {name:'Jareth', description:vecf32([0.1, 0.3, 0.1, 0.2, 0.9])})-[:in]->(book2:Book {name:'Labyrinth'}),
            (:Character {name:'Hoggle', description:vecf32([0.3, 0.2, 0.5, 0.7, 0.9])})-[:in]->(book2)""")

# Find the book with the character description that is most similar (k=1) to the user's query
result = graph.query("""CALL db.idx.vector.queryNodes
                        ('Character', 'description', 1, vecf32([0.1, 0.4, 0.3, 0.2, 0.7])) 
                        YIELD node 
                        MATCH (node)-[]->(b:Book)
                        RETURN b.name AS name""")

print(result.result_set[0][0])
