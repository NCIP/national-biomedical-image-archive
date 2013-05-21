def serverSocket = new ServerSocket(8443);

while(true) {     
    serverSocket.accept { 
        socket ->        
            println "processing new connection..."         
            socket.withStreams { 
                input, output ->             
                    def reader = input.newReader()            
                    def buffer = reader.readLine()             
                    println "server received: $buffer"            
                    now = new Date()             
                    output << "echo-response($now): " + buffer + "\n"        
            }         
            println "processing/thread complete."    
    }
}

