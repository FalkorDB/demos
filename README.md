# FalkorDB Demos

## Step 1 - Run FalkorDB server

```
docker run -p 6379:6379 -it --rm falkordb/falkordb:4.0.0-alpha.1

```

## Step 2 - pick your programming language

### [Python](https://github.com/FalkorDB/demos/tree/main/python)

```bash
cd python
pip install -r requirements.txt
python vectordemo.py
python boltdemo.py
```

### [node](https://github.com/FalkorDB/demos/tree/main/node)

```bash
cd node
npm install
node vectordemo.js
node boltdemo.js
```

### [Java](https://github.com/FalkorDB/demos/tree/main/java)

```bash
cd java
mvn compile assembly:single
java -cp ./target/FalkorDBVectorDemo-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.falkordb.FalkorDBVectorDemo
java -cp ./target/FalkorDBVectorDemo-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.falkordb.BoltVectorDemo
```
