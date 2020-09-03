package demo;

import org.apache.kafka.common.protocol.types.Field;
import org.neo4j.cypher.internal.javacompat.ExecutionEngine;
import org.neo4j.cypher.internal.javacompat.ExecutionResult;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class Neo4jDemo {

    //嵌入式neo4j
    private final static String path = "/Users/xuyang/prog/neo4j-community-3.5.9/data/databases/graph.db";

    private GraphDatabaseService graphDB;

    private enum RelTypes implements RelationshipType{
        CRIME,LINK
    }

    //init
    public void init() {
        graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(new File(path));
        registerShutdownHook(graphDB);
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {

                graphDb.shutdown();
            }
        } );
    }

    public void shutdown(){

        registerShutdownHook( graphDB );
    }

    public void create(){

        Transaction tx= graphDB.beginTx();

        Node case1 = graphDB.createNode(new CaseLabel("CASEINFO"));
        case1.setProperty("name","案例1");
        case1.setProperty("location","西安");

        Node case2 = graphDB.createNode(new CaseLabel("CASEINFO"));
        case2.setProperty("name","案例2");
        case2.setProperty("location","武汉");

        Node userA = graphDB.createNode(new CaseLabel("PERSON"));
        userA.setProperty("name","徐阳");
        userA.setProperty("id","123");

        Node userB = graphDB.createNode(new CaseLabel("PERSON"));
        userB.setProperty("name","？？");
        userB.setProperty("id","111");

        case1.createRelationshipTo(userA,RelTypes.CRIME);
        case2.createRelationshipTo(userA,RelTypes.CRIME);
        case2.createRelationshipTo(userB,RelTypes.LINK);


        tx.success();
        tx.close();
    }

    public void search(String username){

        try(Transaction tx = graphDB.beginTx()){
            // label key-value
            Node startNode = graphDB.findNode(new CaseLabel("PERSON"),"name",username);

            Iterable<Relationship> iterable = startNode.getRelationships(RelTypes.CRIME,Direction.INCOMING);
            for(Relationship r : iterable){
                Node node = r.getStartNode();
                long id = r.getId();
                String name = (String)node.getProperty("name");

                System.out.println(id+" "+name+"////");

                tx.success();
                tx.close();
        }


        }
    }

    public static void main(String[] args) {
        Neo4jDemo neo = new Neo4jDemo();
        neo.init();
        neo.create();
//        neo.search("徐阳");


        neo.shutdown();

    }


}

class CaseLabel implements Label{

    private String name;

    public CaseLabel(String n){
        name = n;
    }

    public String name() {
        return name;
    }
}
