import { createClient, Graph } from 'redis';

const client = createClient();
client.on('error', (err) => console.log('Redis Client Error', err));

await client.connect();

const graph = new Graph(client, 'books');

//  Create Vector index on field description in Character
await graph.query(
    "CREATE VECTOR INDEX FOR (c:Character) ON (c.description) OPTIONS {dim:5, similarityFunction:'euclidean'}"
);

// Fill in the Graph with some data on books and characters
await graph.query(`CREATE 
            (:Character {name:'Bastian Balthazar Bux', description:vector32f([0.1, 0.3, 0.3, 0.4, 0.7])})-[:in]->(book1:Book {name:'The Neverending Story'}),
            (:Character {name:'Atreyu', description:vector32f([0.3, 0.6, 0.2, 0.1, 0.4])})-[:in]->(book1),
            (:Character {name:'Jareth', description:vector32f([0.1, 0.3, 0.1, 0.2, 0.9])})-[:in]->(book2:Book {name:'Labyrinth'}),
            (:Character {name:'Hoggle', description:vector32f([0.3, 0.2, 0.5, 0.7, 0.9])})-[:in]->(book2)`)

// Find the book with the character description that is most similar (k=1) to the user's query
const result = await graph.query(`CALL db.idx.vector.query
                        ({type:'NODE', label:'Character', attribute:'description', query:vector32f([0.1, 0.4, 0.3, 0.2, 0.7]), k:1}) 
                        YIELD entity 
                        MATCH (c:Character)-[]->(b:Book) WHERE c = entity 
                        RETURN b.name AS name`)

console.log(result.data[0]['name'])

await client.disconnect();