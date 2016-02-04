package com.hortonworks.bench;

import com.hortonworks.bench.HiveServerRunner;
import com.hortonworks.bench.QueryQueue;
import java.lang.Thread;

import java.lang.RuntimeException;

public class HiveServerClient{

        public HiveServerClient() {
        }
        
        public static void main(String[] cmdl){
          try {
            String system = cmdl[0];
            String host = cmdl[1];
            String port = cmdl[2];
            String database = cmdl[3];
            String username = cmdl[4];
            String password = cmdl[5];
            String queryList = cmdl[6];
            int threads = Integer.parseInt(cmdl[7]);
            int maxqueries = Integer.parseInt(cmdl[8]);
            int timeout = Integer.parseInt(cmdl[9]);
            
            
            HiveServerRunner.ServerConfiguration configuration = new HiveServerRunner.ServerConfiguration(
                host,
                port,
                database,        
                username,
                password
            );
            
            QueryQueue queue = new QueryQueue(
                queryList, 
                maxqueries 
            );
            
            for (int i = 0; i < threads; i++ ){
                Thread t = new Thread(
                    new HiveServerRunner(
                        new String("Thread-" + i),
                            configuration,
                            queue,
                            timeout
                        )
                );
                t.start();
            }
          } catch (Exception e){ throw new RuntimeException(e.getMessage(), e); }
        }

}
