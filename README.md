# demos

## Python

```bash
cd python
pip install -r requirements.txt
python vectordemo.py
python boltdemo.py
```

## node

```bash
cd node
npm install
node vectordemo.js
node boltdemo.js
```

## Java

```bash
cd java
mvn compile assembly:single
java -cp ./target/FalkorDBVectorDemo-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.falkordb.FalkorDBVectorDemo
java -cp ./target/FalkorDBVectorDemo-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.falkordb.BoltVectorDemo
```
