def socket = new Socket("nciavgridqa5004.nci.nih.gov", 8443);

//def socket = new Socket("localhost", 8443);

socket.withStreams { 
    inn, out -> 
        out << "echo testing ...\n"
        println inn.newReader().readLine()
}