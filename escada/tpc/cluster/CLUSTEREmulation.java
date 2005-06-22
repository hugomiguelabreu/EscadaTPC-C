package escada.tpc.cluster;

import org.apache.log4j.Logger;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;

/**
 * It extends the emulation class defining some methods according to the TPC-C
 * definition. Specifically, it defines the thinktime, keyingtime, process and
 * processIncrement.
 * 
 * @see Emulation
 */
public class CLUSTEREmulation extends Emulation {
	Logger logger = Logger.getLogger(CLUSTEREmulation.class);

	StateObject curTrans = null;

	public void initialize() {
	}

	public long thinkTime() {
		long r = RandGen.negExp(getRandom(), curTrans.getThinkTime() * 1000,
				0.36788, curTrans.getThinkTime() * 1000, 4.54e-5, curTrans
						.getThinkTime() * 1000);
		return (r);
	}

	public long keyingTime() {
		return (curTrans.getKeyingTime());
	}

	public void process(String hid) {
		try {
			while ((getMaxTransactions() == -1) || (getMaxTransactions() > 0)) {
				if (isFinished()) {
					logger.info("Client is returning.");
					return;
				}
				curTrans = getStateTransition().nextState();

				setKeyingTime(keyingTime());
				setThinkTime(getKeyingTime() + thinkTime());

				if (Emulation.getStatusThinkTime()) {
					Thread.sleep(getThinkTime());
				}

				curTrans.requestProcess(this, hid);

				curTrans.postProcess(this, hid);
			}
		} catch (java.lang.InterruptedException it) {
		} catch (java.lang.Exception ex) {
			logger.fatal("Unexpected error. Something bad happend.");
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	public Object processIncrement(String hid) {
		Object trans = null;
		try {
			if (isFinished()) {
				logger.info("Client is returning.");
				return (trans);
			}
			curTrans = getStateTransition().nextState();

			setKeyingTime(keyingTime());
			setThinkTime(getKeyingTime() + thinkTime());

			trans = curTrans.requestProcess(this, hid);

			curTrans.postProcess(this, hid);
		} catch (java.lang.Exception ex) {
			logger.fatal("Unexpected error. Something bad happend.");
			ex.printStackTrace(System.err);
			System.exit(-1);
		}

		return (trans);
	}
}
// arch-tag: 35d76f62-6e73-4ea4-a07a-0207613bf461