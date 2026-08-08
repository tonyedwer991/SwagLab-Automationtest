package Listener;

import org.testng.IExecutionListener;

public class Execlistener implements IExecutionListener {
    @Override
    public void onExecutionStart() {
        System.out.println("onExecutionStart");
//        CMDlistenere.CMDrunner.excutecommand();
    }

    @Override
    public void onExecutionFinish() {
        System.out.println("onExecutionFinish");
//        CMDlistenere.CMDrunner.excutecommand();
    }
}
