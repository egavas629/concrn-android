package software_nation.concrn.android.model;


public class Report {
	public String name;
	public String phone;
	public double latitude;
	public double longitude;
	public String address = "";
	public String neighborhood;
	public String gender;
	public String age;
	public String nature = "";
	public String observations = "";
	public String race;
	public String setting;

	public Report(String name, String phone, double latitude, double longitude,
			String address, String neighborhood, String gender, String age,
			String nature, String observations, String race, String setting) {
		super();
		this.name = name;
		this.phone = phone;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.neighborhood = neighborhood;
		this.gender = gender;
		this.age = age;
		this.nature = nature;
		this.observations = observations;
		this.race = race;
		this.setting = setting;
	}

	public Report(){

	}

}
