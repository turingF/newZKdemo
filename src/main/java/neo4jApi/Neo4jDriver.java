package neo4jApi;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4jDriver  implements AutoCloseable{

    private final Driver driver;
    private final String local_uri = "bolt://localhost:7687";
    private final String local_user = "neo4j";
    private final String local_password = "admin";

    public Neo4jDriver(){
        driver = GraphDatabase.driver(local_uri, AuthTokens.basic(local_user,local_password));
    }

    public Neo4jDriver(String uri, String user, String password){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user,password));

    }


    /**
     * 当发送信息时创建一个neo4j_node,其label为module，属性为<name,$moduleName>
     * @param moduleName
     * @param nodeLabel
     */
    public void CreateNode(String moduleName,String nodeLabel){
        try (Session session = driver.session()){
            String create = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction tx) {
                    if(nodeLabel.equals("module1")){
                        StatementResult result = tx.run( "MERGE (a:module {module:$moduleName}) " +
                                        "SET a.module = $moduleName " +
                                        "RETURN a.module + ', from node ' +id(a)",
                                parameters( "moduleName", moduleName ) );
                        return result.single().get(0).asString();
                    }
                    else if(nodeLabel.equals("topic")){
                        StatementResult result = tx.run( "MERGE (a:topic {module:$moduleName}) " +
                                        "SET a.topicName = $moduleName " +
                                        "RETURN a.topicName + ', from node ' +id(a)",
                                parameters( "moduleName", moduleName ) );
                        return result.single().get(0).asString();
                    }

                    return "create false";
                }
            });
            System.out.println(create);
        }

    }


    /**
     * 创建一条边，前提是两个节点必须存在
     * @param start
     * @param startLabel
     * @param end
     * @param endLabel
     */
    public void CreateEdge(String start,String startLabel,String end,String endLabel){

        try (Session session = driver.session()){
            String create = session.writeTransaction(new TransactionWork<String>(){

                StatementResult result;
                @Override
                public String execute(Transaction tx) {

                    // publish mode
                    if (startLabel.equals("module1") && endLabel.equals("topic")) {

                        result = tx.run("MATCH (a:module),(b:topic)" +
                                        "WHERE a.module = $start AND b.topicName = $end " +
                                        "MERGE (a)-[r:publish]->(b) " +
                                        "RETURN type(r)",
                                parameters("start", start, "end", end));
                        return result.single().get(0).asString();
                    }

                    //subscribe mode
                    else if (startLabel.equals("topic") && endLabel.equals("module1")) {

                            result = tx.run("MATCH (a:topic),(b:module)" +
                                            "WHERE a.topicName = $start AND b.module = $end " +
                                            "MERGE (b)-[r:subscribe]->(a) " +
                                            "RETURN type(r)",
                                    parameters( "start", start, "end", end));

                        return result.single().get(0).asString();

                    }
                    return "create edge false:(";
            }

        });
            System.out.println(create);

    }
    }



    @Override
    public void close() throws Exception {
        driver.close();
    }

}
