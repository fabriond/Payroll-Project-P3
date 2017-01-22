//commissioned employees are paid every 2 Fridays
//salaried employees are paid at the last day of each month (excluding holidays)
//hourly employees are paid every Friday
public class Employee{
	
	public int id;
	public String name;
	public String address;
	public int type; //1 - hourly, 2 - salaried, 3 - commissioned
	public Double commission;
	public int paymentMethod; //1 - mailed check, 2 - hand-delivered check, 3 - bank deposit
	public int paymentFrequency;//1 - weekly, 2 - monthly, 3 - biweekly
	public boolean syndicateMember;
	public int syndicateId = -1;
	public Double syndicateTax = 0.0;
	public Double serviceTax = 0.0;
	public Double workedHours = 0.0;//only for hourly employees
	public Double salary = 0.0;
	public boolean active; //serves for "undo" in case of removal
	public Sale[] sales;
	public int saleNumber = 0;
	public int maxSales;

}