import org.rsna.filesender.CmdLineSender;


def dicomFile = "c:/0000000A"
def ctpImportUrl = "http://localhost:23104"
def quarantineFile = "c:/main.hs"

def submitDicomFile(dicomFile, quarantineFile, ctpImportUrl, numTimes) {
    def quarantineFactor = 5
    for(def i in 1..numTimes) {
        if(i % quarantineFactor == 0) {
            new CmdLineSender(new File(quarantineFile), ctpImportUrl, true);
        }
        else {
            new CmdLineSender(new File(dicomFile), ctpImportUrl, true);
        }
    }
}

for(def i in 1..10) {
    Thread.start {
        submitDicomFile(dicomFile, quarantineFile, ctpImportUrl, 10000);
    }
}    


