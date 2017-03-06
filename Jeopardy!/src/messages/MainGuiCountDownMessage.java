package messages;


public class MainGuiCountDownMessage implements Message {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long count;

    public MainGuiCountDownMessage(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }
}

