package escada.tpc.cluster;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;
import escada.tpc.cluster.database.transaction.dbCLUSTERDatabase;


/**
* It implements the states according to the definition  of the TPC-C. Basically, it setups import information used in the execution of the delivery transaction.
 Additionally, it defines the trace flag, which is a boolean value used to log traces or not and the trace file.
**/
public class UpdateTrans
    extends StateObject {

	public void initProcess(Emulation em,String hid) {
    outInfo.putInfo("resubmit",Boolean.toString(Emulation.getStatusReSubmit()));
    outInfo.putInfo("trace", Emulation.getTraceInformation());
    outInfo.putInfo("abort", "0");
    outInfo.putInfo("hid", hid);    
    
    int crid = RandGen.nextInt(em.getRandom(), 1, CLUSTERConst.rngCarrier + 1);
    outInfo.putInfo("crid", Integer.toString(crid));
    outInfo.putInfo("thinktime", Long.toString(em.getThinkTime()));
    outInfo.putInfo("file", em.getEmulationName());
  }

  public void prepareProcess(Emulation em,String hid) {

  }

  public Object requestProcess(Emulation em,String hid) {
    Object requestProcess = null;
    dbCLUSTERDatabase db = (dbCLUSTERDatabase) em.getDatabase();
    try {
      initProcess(em,hid);
      requestProcess =  db.TraceUpdateTrans(outInfo,hid);
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
    return (requestProcess);
  }

  public void postProcess(Emulation em,String hid) {
    inInfo.resetInfo();
    outInfo.resetInfo();
  }

  public void setProb() {
    prob = 50;
  }

  public void setKeyingTime() {
    keyingtime = 2;
  }

  public void setThinkTime() {
    thinktime = 5;
  }

  public String toString() {
    return ("UpdateTrans");
  }
}
// arch-tag: a1847dc2-63ac-4335-9374-35a7ac9d3a2c
