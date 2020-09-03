package demo;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class Neo4jDemo2 {

    private final static String path = "~/prog/neo4j-community-3.5.9/data/databases/graph.db";
    private GraphDatabaseService graphDb;


    private enum RelTypes implements RelationshipType {
        RELEASED
    }

    //根据database唯一创建server，可多线程使用sever
    public void init() {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(path));

    }

    public void closeDB(){
        graphDb.shutdown();
    }

    public void create(){
        try (Transaction tx = graphDb.beginTx()) {
            // 创建标签
            Label label1 = Label.label("Musician");
            Label label2 = Label.label("Album");
            // 创建节点
            Node node1 = graphDb.createNode(label1);
            node1.setProperty("name", "Jay Chou");
            Node node2 = graphDb.createNode(label2);
            node2.setProperty("name", "Fantasy");
            // 创建关系及属性
            Relationship relationship = node1.createRelationshipTo(node2, RelTypes.RELEASED);
            relationship.setProperty("date", "2001-09-14");
            // 结果输出
            System.out.println("created node name is" + node1.getProperty("name"));
            System.out.println(relationship.getProperty("date"));
            System.out.println("created node name is" + node2.getProperty("name"));
            // 提交事务
            tx.success();
            tx.close();
        }

    }

    public void search(){
        try (Transaction tx = graphDb.beginTx()) {
            // 查询节点
            Label label = Label.label("Musician");
            Node node = graphDb.findNode(label, "name", "Jay Chou");
            System.out.println("query node name is " + node.getProperty("name"));
            // 更新节点
            node.setProperty("birthday", "1979-01-18");
            System.out.println(node.getProperty("name") + "'s birthday is " + node.getProperty("birthday", new String()));
            // 提交事务
            tx.success();
        }
    }

    public static void main(String[] args) {
        Neo4jDemo2 demo2 = new Neo4jDemo2();
        demo2.init();
        //demo2.create();
        demo2.search();

        demo2.closeDB();
    }


}
