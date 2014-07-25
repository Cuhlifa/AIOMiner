package scripts.MinerPlusPlus;

public class Offer {
	private String name;
	private int price;
	private boolean selling;
	private String rsName;
	private int quantity;
	private String notes;
	private int date;
	private String contact;
	private boolean validated;

	public Offer(JSONObject offer, String name) {
		try {
			this.name = name;
			price = offer.getInt("price");
			selling = (offer.getInt("selling") == 1);
			rsName = offer.getString("rs_name");
			quantity = offer.getInt("quantity");
			notes = offer.getString("notes");
			date = offer.getInt("date");
			contact = offer.getString("contact");
			setValidated((offer.getInt("validated") == 1));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return How to contact this offer CC or PM, CC meaning clan chat and PM
	 *         meaning private message
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @return the time the offer was placed as a unix date time
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @return the name of the offer item within the buy order or sell order
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the notes the player left on the offer
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @return the price of the offers buy order or sell order
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return quantity of the offer
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @return the runescape username associated with the offer
	 */
	public String getRsName() {
		return rsName;
	}

	/**
	 * @return <code>true</code> if the offer is a sell order <code>false</code>
	 *         if the offer is a buy order
	 */
	public boolean isSelling() {
		return selling;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setRsName(String rsName) {
		this.rsName = rsName;
	}

	public void setSelling(boolean selling) {
		this.selling = selling;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}
}
