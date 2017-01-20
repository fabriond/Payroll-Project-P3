import java.util.Scanner;

public class Payroll {
	
	static Scanner scan = new Scanner(System.in);
	static int employeeCount = 0;
	static Employee[] employees = new Employee[200];
	
	public static void main(String[] args){
		
		int menuOption = 0;
		
		while(menuOption != 10){
			
			System.out.println("Select your action: \n");
			System.out.println("  1 - Add Employee ");
			System.out.println("  2 - Remove Employee ");
			System.out.println("  3 - Turn in Time Card ");
			System.out.println("  4 - Turn in Sale Results ");
			System.out.println("  5 - Turn in Service Tax ");
			System.out.println("  6 - Edit Employee ");
			System.out.println("  7 - Run Today's Payroll ");
			System.out.println("  8 - Undo ");
			System.out.println("  9 - Redo ");
			System.out.println("  10 - Close \n");
			
			menuOption = scan.nextInt();
			
			if(menuOption == 1){
				addEmployees();
			}
			
			if(menuOption == 2){
				removeEmployee();
			}
			
			if(menuOption >= 3 && menuOption != 10){
				System.out.println("Function still not implemented");	
			}
		
		}

	}
	
	public static void addEmployees(){
		if(employeeCount <= 200){
			
			Employee newEmployee = new Employee();
			
			System.out.print("Name: ");
			scan.nextLine();
			newEmployee.name = scan.nextLine();
			System.out.println("Employee's Name: "+newEmployee.name);
			
			System.out.print("Address: ");
			newEmployee.address = scan.nextLine();
			System.out.println("Employee's Address: "+newEmployee.address);
			
			System.out.println("Type: ");
			System.out.println("  1 - Hourly ");
			System.out.println("  2 - Salaried ");
			System.out.println("  3 - Commissioned ");
			newEmployee.type = scan.nextInt();
			
			while(newEmployee.type > 3 || newEmployee.type < 1){
				
				System.out.println("Type entered is not valid, try again. \n");						
				System.out.println("Type: ");
				System.out.println("  1 - Hourly ");
				System.out.println("  2 - Salaried ");
				System.out.println("  3 - Commissioned ");
				newEmployee.type = scan.nextInt();
				
			}
			System.out.println("Employee's Type: "+newEmployee.type);
			
			if(newEmployee.type == 1) System.out.print("Hourly Salary: R$ ");
			else if(newEmployee.type == 2) System.out.print("Monthly Salary: R$ ");
			else if(newEmployee.type == 3) {
				
				System.out.print("Commission: ");
				newEmployee.commission = scan.nextDouble();
				System.out.print("Monthly Salary: R$ ");
				
			}
			newEmployee.salary = scan.nextDouble();
			
			System.out.println("Payment Method: ");
			System.out.println("  1 - Mailed check ");
			System.out.println("  2 - Hand-delivered Check ");
			System.out.println("  3 - Bank Deposit ");
			newEmployee.paymentMethod = scan.nextInt();
			
			newEmployee.id = employeeCount;
			System.out.println("");
			newEmployee.active = true;
			
			employees[employeeCount] = newEmployee;
			
			employeeCount++;
			
			
		}
	
		else System.out.println("This Company Cannot Hire More Employees");
		
		return;
	
	}
	
	public static void removeEmployee(){
		System.out.print("Employee's id: ");
		int removeId = scan.nextInt();
		
		System.out.println("Are you sure you want to remove "+employees[removeId].name+" from the employees?\n(answer yes or no)");
		String answer = scan.next();
		
		if(answer.equals("yes")){
			employees[removeId].active = false;
			System.out.println("Employee removed successfully!");
			return;
		}
		else return;
		
	}
	
	public static void addTimeCard(){
		
		System.out.print("Worked Hours: ");
		int time = scan.nextInt();
		
		System.out.print("Employee's id: ");
		int timeCardId = scan.nextInt();
		
		System.out.println("Are you sure this time card is from "+employees[timeCardId].name+"?\n(answer yes or no)");
		String answer = scan.next();
		
		while(answer.equals("no")){
			System.out.print("Employee's id: ");
			timeCardId = scan.nextInt();
			System.out.println("Are you sure this time card is from "+employees[timeCardId].name+"?\n(answer yes or no)");
			answer = scan.next();
		}
		
		if(answer.equals("yes")){
			employees[timeCardId].workedHours += time;
			System.out.println("Time card successfully added to"+employees[timeCardId].name+"!");
		}
		return;
	}
	
	public static void saleResult(){
		
		Sale newSale = new Sale();
		int employeeId;		
		System.out.print("Date of sale: ");
		newSale.date = scan.nextLine();
				
		System.out.print("Value of sale: ");
		newSale.value = scan.nextDouble();
		
		System.out.print("Employee's id: ");
		employeeId = scan.nextInt();
		
		System.out.println("Are you sure this sale is from "+employees[employeeId].name+"?\n(answer yes or no)");
		String answer = scan.next();
		
		while(answer.equals("no")){
			System.out.print("Employee's id: ");
			employeeId = scan.nextInt();
			System.out.println("Are you sure this sale is from "+employees[employeeId].name+"?\n(answer yes or no)");
			answer = scan.next();
		}
		
		if(answer.equals("yes")){
			employees[employeeId].sales[employees[employeeId].saleNumber] = newSale;
			System.out.println("Time card successfully added to"+employees[employeeId].name+"!");
		}
		return;
		
	}
	
	
	
}
