package Utilities;

public class CMDlistenere {
    public class CMDrunner {
        public static int excutecommand(String command) {
            int exitcode = -1;
            try {
                Process process = Runtime.getRuntime().exec(command);
                exitcode = process.exitValue();
                if (exitcode != 0) {
                    System.err.println("Exit code : " + exitcode);
                } else {
                    System.err.println("Exit code : " + exitcode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return exitcode;
        }
    }
}
