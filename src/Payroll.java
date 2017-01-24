import java.util.Calendar;
import java.util.Scanner;

public class Payroll {
	
	static Scanner scan = new Scanner(System.in);
	static int syndicateMembers = 0;
	static int employeeCount = 0;
	static Employee[] employees = new Employee[200];
	static int lastAction = 0;
	static int lastMonth = 0;
	static int lastDayOfLastMonth = 0;
	static int lastPayment = 0;
	static int lastBiweeklyPayment = 0;
	static int activeEmployeeCount = 0;
	
	//used for undo/redo purposes:
	static int lastUndo = 0;
	static int lastEmployeeChanged = -1;
	static Double lastTimeCard;
	static int lastServiceTax;
	static int lastEdited;
	static String lastEditedString;
	static String auxString;
	static int lastEditedInt;
	static int lastAgendaOption;
	static int lastAgendaPaymentOption;
	static int auxInt;
	static Double lastEditedSalary;
	static Double lastEditedCommission;
	static Double auxDouble;
	static boolean lastEditedSyndicateStatus;
	static boolean auxBoolean;
	static Double lastEditedSyndicateTax;
	static Sale lastDeletedSale;
	
	
	public static void main(String[] args){
				
		int menuOption = 0;
		System.out.println("Create default employees?");
		System.out.println("  1 - Yes");
		System.out.println("  2 - No");
		if(scan.nextInt() == 1) defaultEmployees();
		
		while(menuOption != 11){
			
			System.out.println("Select your action: \n");
			System.out.println("  0 - List All Employees");
			System.out.println("  1 - Add Employee ");
			System.out.println("  2 - Remove Employee ");
			System.out.println("  3 - Turn in Time Card ");
			System.out.println("  4 - Turn in Sale Results ");
			System.out.println("  5 - Turn in Service Tax ");
			System.out.println("  6 - Edit Employee ");
			System.out.println("  7 - Run Today's Payroll ");
			System.out.println("  8 - Undo ");
			System.out.println("  9 - Redo ");
			System.out.println("  10 - Create New Payment Agenda ");
			System.out.println("  11 - Close \n");
			
			menuOption = scan.nextInt();
			while(lastAction != 8 && menuOption == 9){
				System.out.println("You can't redo an action you haven't undone.\n");
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
				System.out.println("  10 - Create New Payment Agenda ");
				System.out.println("  11 - Close \n");
				menuOption = scan.nextInt();
			}
			
			if(((lastAction == 2 && menuOption != 8) || (lastUndo == 1 && menuOption != 9)) && menuOption != 0) deleteEmployee();
			
			if(menuOption == 0){
				printCurrentEmployees(4);
				System.out.println("");
			}
			
			else if(menuOption == 1){
				addEmployees();
			}
			
			else if(menuOption == 2){
				removeEmployee(); 
			}
			
			else if(menuOption == 3){
				addTimeCard();
			}
			
			else if(menuOption == 4){
				saleResult();
			}
			
			else if(menuOption == 5){
				serviceTax();
			}
			
			else if(menuOption == 6){
				editEmployee();
			}
			
			else if(menuOption == 7){
				payroll();
			}
			
			else if(menuOption == 8){
				System.out.println(lastAction);
				if(lastAction == 8) System.out.println("You can't undo twice in a row, try using redo instead.");
				else if(lastAction == 9) undo(1);
				else undo(0);				
			}
			
			else if(menuOption == 9){
				if(lastAction == 9) System.out.println("You can't redo twice in a row, try using undo instead.");
				else redo();				
			}
			
			else if(menuOption == 10){
				createNewAgenda();				
			}
			
			else if(menuOption != 11){
				System.out.println("Function still not implemented");	
			}
			
			if(menuOption != 0) lastAction = menuOption;
		
		}

	}
	
	//name, address, type, salary, payment method, syndicate membership, syndicate tax
	
	public static void addEmployees(){
		if(employeeCount < 200){
			
			Employee newEmployee = new Employee();
			newEmployee.saleNumber = 0;
			System.out.print("Name: ");
			scan.nextLine();
			newEmployee.name = scan.nextLine();
			
			System.out.print("Address: ");
			newEmployee.address = scan.nextLine();
			
			System.out.println("Type: ");
			System.out.println("  1 - Hourly ");
			System.out.println("  2 - Salaried ");
			System.out.println("  3 - Commissioned ");
			newEmployee.type = scan.nextInt();
			
			while(newEmployee.type > 3 || newEmployee.type < 1){
				
				System.out.println("Type entered is not valid, try again.\n");						
				System.out.println("Type: ");
				System.out.println("  1 - Hourly ");
				System.out.println("  2 - Salaried ");
				System.out.println("  3 - Commissioned ");
				newEmployee.type = scan.nextInt();
				
			}
			
			newEmployee.paymentFrequency = newEmployee.type;
			
			if(newEmployee.type == 1) System.out.print("Hourly Salary: R$ ");
			else if(newEmployee.type == 2) System.out.print("Monthly Salary: R$ ");
			else if(newEmployee.type == 3) {
				
				System.out.print("Commission(Percentage): ");
				newEmployee.commission = scan.nextDouble();
				System.out.print("Bi-weekly Salary: R$ ");
				
			}
			newEmployee.salary = scan.nextDouble();
			
			System.out.println("Payment Method: ");
			System.out.println("  1 - Mailed check ");
			System.out.println("  2 - Hand-delivered Check ");
			System.out.println("  3 - Bank Deposit ");
			newEmployee.paymentMethod = scan.nextInt();
			
			while(newEmployee.paymentMethod > 3 || newEmployee.paymentMethod < 1){
				
				System.out.println("Payment Method entered is not valid, try again.\n");						
				System.out.println("Payment Method: ");
				System.out.println("  1 - Mailed check ");
				System.out.println("  2 - Hand-delivered Check ");
				System.out.println("  3 - Bank Deposit ");
				newEmployee.paymentMethod = scan.nextInt();
				
			}
			
			while(true){
				System.out.println("Is this employee a syndicate member?");
				System.out.println("  1 - Yes");
				System.out.println("  2 - No");
				int answer = scan.nextInt();
				if(answer == 1){
					newEmployee.syndicateMember = true;
					newEmployee.syndicateId = syndicateMembers;
					syndicateMembers++;
					System.out.print("Syndicate Tax: R$ ");
					newEmployee.syndicateTax = scan.nextDouble();
					break;
				}
				else if(answer == 2){
					newEmployee.syndicateMember = false;
					break;
				}
				else System.out.println("Invalid option, try again.\n");
			
			}
			
			newEmployee.id = employeeCount;
			
			newEmployee.active = true;
			newEmployee.saleNumber = 0;
			
			employees[employeeCount] = newEmployee;
			lastEmployeeChanged = employeeCount;
			employeeCount++;
			activeEmployeeCount++;
			
			System.out.println("Employee added successfully!\n");
			
		}
	
		else System.out.println("This Company can't Hire More Employees");
		
		return;
	
	}
	
	public static void removeEmployee(){
		if(activeEmployeeCount == 0){
			System.out.println("Add at least one employee!\n");
			return;
		}
		printCurrentEmployees(0);
		
		int removeId = idValidation();
		
		removeActive(removeId);
		System.out.println("Employee removed successfully!\n");
		return;
		
	}
	
	public static void removeActive(int id){
		employees[id].active = false;
		activeEmployeeCount--;
		return;
	}
	public static void addActive(int id){	
		employees[lastEmployeeChanged].active = true;
		activeEmployeeCount++;
		return;
	}
	
	public static void deleteEmployee(){
		Employee[] newEmployeeList = new Employee[200];
		for(int i = 0, j = 0; i < employeeCount; i++){
			if(employees[i].active == true){
				newEmployeeList[j] = employees[i];
				newEmployeeList[j].id = j;
				j++;
			}
		}
		employeeCount = activeEmployeeCount;
		employees = newEmployeeList;
		return;
		
	}
	
	public static void addTimeCard(){
		
		if(activeEmployeeCount == 0){
			System.out.println("Add at least one employee!\n");
			return;
		}
		
		printCurrentEmployees(1);
		int employeeId = idValidation();
		
		while(employees[employeeId].type != 1){
			System.out.println("Employee selected can't have a time card added, try again.\n");
			printCurrentEmployees(1);
			employeeId = idValidation();
		}
		System.out.print("Worked Hours: ");
		Double time = scan.nextDouble();
		if(time > 8.0){
			Double extraTime = (time - 8.0)*1.5;
			time = 8.0 + extraTime;
		}
		lastTimeCard = time;
		employees[employeeId].workedHours += time;
		System.out.println("Time card successfully added to "+employees[employeeId].name+"!\n");
		
		return;
	}
	
	public static void saleResult(){
		
		if(activeEmployeeCount == 0){
			System.out.println("Add at least one employee!\n");
			return;
		}
		
		printCurrentEmployees(2);
		
		int employeeId = idValidation();
		
		while(employees[employeeId].type != 3){
			System.out.println("Employee selected can't have a sale results added, try again.\n");
			printCurrentEmployees(2);
			employeeId = idValidation();
		}
		
		if(employees[employeeId].saleNumber == employees[employeeId].maxSales && employees[employeeId].saleNumber != 0){
			employees[employeeId].maxSales += 200;
			Sale[] newSales = new Sale[employees[employeeId].maxSales];
			for(int i = 0; i < employees[employeeId].saleNumber; i++) newSales[i] = employees[employeeId].sales[i];
			employees[employeeId].sales = newSales;
		}
		else if(employees[employeeId].saleNumber == 0){
			employees[employeeId].maxSales = 200;
			employees[employeeId].sales = new Sale[employees[employeeId].maxSales];
		}
		
		Sale newSale = new Sale();	
		System.out.print("Date of sale: ");
		scan.nextLine();
		newSale.date = scan.nextLine();
		
		
		String[] parts = newSale.date.split("/");
		String day = parts[0];
		String month = parts[1];
		String year = parts[2];
		
		newSale.day = Integer.parseInt(day);
		newSale.month = Integer.parseInt(month);
		newSale.year = Integer.parseInt(year);
		
		System.out.print("Value of sale: R$ ");
		newSale.value = scan.nextDouble();
		
		employees[employeeId].sales[employees[employeeId].saleNumber] = newSale;
		employees[employeeId].saleNumber++;
		
		System.out.println("Sale successfully added to "+employees[employeeId].name+"!\n");
		
		return;
		
	}
	
	public static void serviceTax(){
		
		if(activeEmployeeCount == 0){
			System.out.println("Add at least one employee!\n");
			return;
		}
		
		printCurrentEmployees(3);		
		int employeeId = idValidation();
		
		while(employees[employeeId].syndicateMember != true){
			System.out.println("Employee selected can't have service taxes added because he isn't a syndicate member, try again.\n");
			printCurrentEmployees(3);
			employeeId = idValidation();
		}
		
		Double newTax = 0.0;
		newTax = employees[employeeId].serviceTax;
		System.out.print("Service Tax Value: R$ ");
		newTax += scan.nextDouble();
		employees[employeeId].serviceTax = newTax;
		System.out.print("Service Tax successfully added to "+employees[employeeId].name+"!\n");
		return;
		
	}
	
	public static void editEmployee(){
		
		if(activeEmployeeCount == 0){
			System.out.println("Add at least one employee!\n");
			return;
		}
		
		printCurrentEmployees(0);
		
		int employeeId = idValidation();
		int entry = 0;
		while(entry > 7 || entry < 1){
			System.out.println("What would you like to edit?");
			System.out.println("  1 - Name");
			System.out.println("  2 - Address");
			System.out.println("  3 - Type");
			System.out.println("  4 - Salary/Commission");
			System.out.println("  5 - Payment Method");
			System.out.println("  6 - Syndicate Membership");
			System.out.println("  7 - Payment Frequency");
			entry = scan.nextInt();
			if(entry > 7 || entry < 1) System.out.println("Entry not valid, try again.\n");
		}
		lastEdited = entry;
		if(entry == 1){
			System.out.println("Current Name: "+employees[employeeId].name);
			lastEditedString = employees[employeeId].name;
			System.out.print("New Name: ");
			scan.nextLine();
			employees[employeeId].name = scan.nextLine();
		}
		else if(entry == 2){
			System.out.println("Current Address: "+employees[employeeId].address);
			lastEditedString = employees[employeeId].address;
			System.out.print("New Address: ");
			scan.nextLine();
			employees[employeeId].address = scan.nextLine();
		}
		else if(entry == 3){
			int startingType = employees[employeeId].type;
			lastEditedInt = startingType;
			lastEditedCommission = employees[employeeId].commission;
			lastEditedSalary = employees[employeeId].salary;			
			
			System.out.println("Current Type: "+employees[employeeId].type+" - "+typeConversion(employees[employeeId].type));
			System.out.println("New type: ");
			System.out.println("  1 - Hourly ");
			System.out.println("  2 - Salaried ");
			System.out.println("  3 - Commissioned ");
			employees[employeeId].type = scan.nextInt();
			while(employees[employeeId].type > 3 || employees[employeeId].type < 1){
				
				System.out.println("Type entered is not valid, try again.\n");						
				System.out.println("New Type: ");
				System.out.println("  1 - Hourly ");
				System.out.println("  2 - Salaried ");
				System.out.println("  3 - Commissioned ");
				employees[employeeId].type = scan.nextInt();
				
			}
			
			if(employees[employeeId].type == 3 && startingType != 3){
				lastEditedCommission = employees[employeeId].commission;
				System.out.print("Commission(Percentage): ");
				employees[employeeId].commission = scan.nextDouble();
				if(startingType == 2) employees[employeeId].salary /= 2;
			}
			else if(employees[employeeId].type != 3 && startingType == 3){
				employees[employeeId].commission = null;
				if(employees[employeeId].type == 2) employees[employeeId].salary *= 2;
			}
			
			if((employees[employeeId].type != 1 && startingType == 1) || (employees[employeeId].type == 1 && startingType != 1)){
				System.out.print("New Salary: R$ ");
				employees[employeeId].salary = scan.nextDouble();
			}

		}
		else if(entry == 4){
			
			lastEditedCommission = employees[employeeId].commission;
			lastEditedSalary = employees[employeeId].salary;
			
			System.out.println("Current Salary: R$ "+employees[employeeId].salary+" "+paymentRate(employees[employeeId].paymentFrequency));
			System.out.print("New Salary: R$ ");
			employees[employeeId].salary = scan.nextDouble();
			if(employees[employeeId].type == 3){
				System.out.println("Current Commission: "+employees[employeeId].commission+"%");
				System.out.print("New Commission(Percentage): ");
				employees[employeeId].commission = scan.nextDouble();
			}
		}
		else if(entry == 5){
			
			lastEditedInt = employees[employeeId].paymentMethod;
			
			System.out.println("Current Payment Method: "+employees[employeeId].paymentMethod+" - "+paymentMethodConversion(employees[employeeId].paymentMethod));
			System.out.println("New Payment Method: ");
			System.out.println("  1 - Mailed check ");
			System.out.println("  2 - Hand-delivered Check ");
			System.out.println("  3 - Bank Deposit ");
			employees[employeeId].paymentMethod = scan.nextInt();
			
			while(employees[employeeId].paymentMethod > 3 || employees[employeeId].paymentMethod < 1){
				
				System.out.println("Payment Method entered is not valid, try again.\n");						
				System.out.println("New Payment Method: ");
				System.out.println("  1 - Mailed check ");
				System.out.println("  2 - Hand-delivered Check ");
				System.out.println("  3 - Bank Deposit ");
				employees[employeeId].paymentMethod = scan.nextInt();
				
			}
			
		}
		else if(entry == 6){
			editSyndicateOptions(employeeId);			
		}
		
		else if(entry == 7){
			int oldFrequency = employees[employeeId].paymentFrequency;
			lastEditedInt = oldFrequency;
			lastEditedSalary = employees[employeeId].salary;
			System.out.println("Current Payment Frequency: "+employees[employeeId].paymentFrequency+" - "+paymentRate(employees[employeeId].paymentFrequency));
			System.out.println("New Payment Frequency: ");
			System.out.println("  1 - Weekly ");
			System.out.println("  2 - Monthly ");
			System.out.println("  3 - Bi-weekly ");
			employees[employeeId].paymentFrequency = scan.nextInt();
			
			while(employees[employeeId].paymentFrequency > 3 || employees[employeeId].paymentFrequency < 1){
				
				System.out.println("Current Payment Frequency: "+employees[employeeId].paymentFrequency+" - "+paymentRate(employees[employeeId].paymentFrequency));
				System.out.println("New Payment Frequency: ");
				System.out.println("  1 - Weekly ");
				System.out.println("  2 - Monthly ");
				System.out.println("  3 - Bi-weekly ");
				employees[employeeId].paymentFrequency = scan.nextInt();
				
			}
			if(oldFrequency != employees[employeeId].paymentFrequency){
				System.out.println("Current Salary: R$ "+employees[employeeId].salary+" "+paymentRate(employees[employeeId].paymentFrequency));
				System.out.print("New Salary: R$ ");
				employees[employeeId].salary = scan.nextDouble();
			}
			employees[employeeId].lastPayment = 0;
		}
		System.out.println("Employee edited successfully!\n");
		return;
		
	}
		
	public static void payroll(){
		
		if(activeEmployeeCount == 0){
			System.out.println("Add at least one employee!\n");
			return;
		}
		
		int today = getDate();
		int weekDay = getWeekDay();
		int weeklyPayment = today + (6 - weekDay);
		int monthlyPaymentDay = lastWorkingDayOfMonth();
		int biweeklyPaymentDay = secondFridayOfMonth();
		int paidEmployees = 0;
		
		for(int i = 0; i < employeeCount; i++){
			if(employees[i].monthPayment == 0 && employees[i].weekPayment == 0){
				if(employees[i].paymentFrequency == 1 && today == weeklyPayment){
					paidEmployees++;
					System.out.println("Paid "+employees[i].name+": R$ "+calculatePayCheck(employees[i])+" via "+paymentMethodConversion(employees[i].paymentMethod)+"\n");
				}
				else if(employees[i].paymentFrequency == 2 && today == monthlyPaymentDay){
					paidEmployees++;
					System.out.println("Paid "+employees[i].name+": R$ "+calculatePayCheck(employees[i])+" via "+paymentMethodConversion(employees[i].paymentMethod)+"\n");
				}
				else if(employees[i].paymentFrequency == 3 && today == biweeklyPaymentDay){
					paidEmployees++;
					System.out.println("Paid "+employees[i].name+": R$ "+calculatePayCheck(employees[i])+" via "+paymentMethodConversion(employees[i].paymentMethod)+"\n");
				}
			}
			else if(employees[i].monthPayment == today && employees[i].paymentFrequency == 2){
				paidEmployees++;
				System.out.println("Paid "+employees[i].name+": R$ "+calculatePayCheck(employees[i])+" via "+paymentMethodConversion(employees[i].paymentMethod)+"\n");
			}
			else if(employees[i].weekPayment == weekDay){
				paidEmployees++;
				System.out.println("Paid "+employees[i].name+": R$ "+calculatePayCheck(employees[i])+" via "+paymentMethodConversion(employees[i].paymentMethod)+"\n");
			}
			else if(secondSelectDayOfMonth(employees[i].weekPayment, employees[i]) == today){
				paidEmployees++;
				System.out.println("Paid "+employees[i].name+": R$ "+calculatePayCheck(employees[i])+" via "+paymentMethodConversion(employees[i].paymentMethod)+"\n");
			}
			
			
		}	
		
		if(paidEmployees == 0){
			System.out.println("No employees are paid today.\n");
		}
		setLastPayment();
		setMonth();
		return;
		
	}
	
	public static Double calculatePayCheck(Employee employee){
		Double payCheck = 0.0;
		int month = getMonth();
		if(employee.type == 1) payCheck = employee.salary * employee.workedHours;
		else if(employee.type == 2) payCheck = employee.salary;
		else if(employee.type == 3){
			Double totalSalesMoney = 0.0;
			for(int i = 0; i < employee.saleNumber; i++){
				if((employee.sales[i].day * employee.sales[i].month * employee.sales[i].year) - 14 < lastPayment){
					totalSalesMoney += employee.sales[i].value*(employee.commission/100);
				}
			}
			payCheck = employee.salary + totalSalesMoney; 
		}
		
		if(month != lastMonth && employee.syndicateMember == true) payCheck -= (employee.syndicateTax+employee.serviceTax);
		return payCheck;
		
	}
	
	public static void undo(int option){
		if(option == 1){
			auxInt = lastAction;
			lastAction = lastUndo;
		}
		
		if(lastEmployeeChanged == -1){
			System.out.println("There are no actions to be undone!");
			return;
		}
		
		else if(lastAction == 1){
			removeActive(lastEmployeeChanged);
		}
	
		else if(lastAction == 2){
			addActive(lastEmployeeChanged);
		}
	
		else if(lastAction == 3){
			employees[lastEmployeeChanged].workedHours -= lastTimeCard;
		}
	
		else if(lastAction == 4){
			lastDeletedSale = employees[lastEmployeeChanged].sales[employees[lastEmployeeChanged].saleNumber]; 
			employees[lastEmployeeChanged].sales[employees[lastEmployeeChanged].saleNumber] = null;
			employees[lastEmployeeChanged].saleNumber--;
		}
		
		else if(lastAction == 5){
			employees[lastEmployeeChanged].serviceTax -= lastServiceTax;
		}
		
		else if(lastAction == 6){
			undoEdit();
		}
		
		else if(lastAction == 7){
			System.out.println("The employees have already been paid, payroll runs can't be undone or redone!");
		}
		
		else if(lastAction == 10){
			auxInt = employees[lastEmployeeChanged].paymentFrequency; 
			employees[lastEmployeeChanged].paymentFrequency = lastEditedInt;
			lastEditedInt = auxInt;
			
			auxInt = employees[lastEmployeeChanged].weekPayment;
			employees[lastEmployeeChanged].weekPayment = lastAgendaOption;
			lastAgendaOption = auxInt;
			if(lastEdited == 2){
				auxInt = employees[lastEmployeeChanged].lastPayment;
				employees[lastEmployeeChanged].lastPayment = lastAgendaPaymentOption;
				lastAgendaPaymentOption = auxInt;
			}	
		}
		
		lastUndo = lastAction;
		if(option == 1) lastAction = auxInt;
		
	}
	
	public static void undoEdit(){
		if(lastEdited == 1){
			auxString = employees[lastEmployeeChanged].name;
			employees[lastEmployeeChanged].name = lastEditedString;
			lastEditedString = auxString;
		}
		
		else if(lastEdited == 2){
			auxString = employees[lastEmployeeChanged].address;
			employees[lastEmployeeChanged].address = lastEditedString;
			lastEditedString = auxString;
		}
		
		else if(lastEdited == 3){
			auxInt = employees[lastEmployeeChanged].type;
			employees[lastEmployeeChanged].type = lastEditedInt;
			lastEditedInt = auxInt;
			
			auxDouble = employees[lastEmployeeChanged].commission;
			employees[lastEmployeeChanged].commission = lastEditedCommission;
			lastEditedCommission = auxDouble;
			
			auxDouble = employees[lastEmployeeChanged].salary;
			employees[lastEmployeeChanged].salary = lastEditedSalary;
			lastEditedSalary = auxDouble;
		}
		
		else if(lastEdited == 4){
			auxDouble = employees[lastEmployeeChanged].commission;
			employees[lastEmployeeChanged].commission = lastEditedCommission;
			lastEditedCommission = auxDouble;
			
			auxDouble = employees[lastEmployeeChanged].salary;
			employees[lastEmployeeChanged].salary = lastEditedSalary;
			lastEditedSalary = auxDouble;			
		}
		
		else if(lastEdited == 5){
			auxInt = employees[lastEmployeeChanged].paymentMethod;
			employees[lastEmployeeChanged].paymentMethod = lastEditedInt;
			lastEditedInt = auxInt;
		}
		
		else if(lastEdited == 6){
			auxBoolean = employees[lastEmployeeChanged].syndicateMember;
			auxDouble = employees[lastEmployeeChanged].syndicateTax;
			employees[lastEmployeeChanged].syndicateMember = lastEditedSyndicateStatus;
			employees[lastEmployeeChanged].syndicateTax = lastEditedSyndicateTax;
			lastEditedSyndicateStatus = auxBoolean;
			lastEditedSyndicateTax = auxDouble;
		}
		
		else if(lastEdited == 7){
			auxInt = employees[lastEmployeeChanged].paymentFrequency;
			auxDouble = employees[lastEmployeeChanged].salary;
			employees[lastEmployeeChanged].paymentFrequency = lastEditedInt;
			employees[lastEmployeeChanged].salary = lastEditedSalary;
			lastEditedInt = auxInt;
			lastEditedSalary = auxDouble;
		}
		
	}
	/*
	  		Edit Options Order
			1 - Name
			2 - Address
			3 - Type
			4 - Salary/Commission
			5 - Payment Method
			6 - Syndicate Membership
			7 - Payment Frequency
	*/
	
	public static void redo(){
		
		if(lastUndo == 1){
			addActive(lastEmployeeChanged);
		}
	
		else if(lastUndo == 2){
			removeActive(lastEmployeeChanged);
		}
	
		else if(lastUndo == 3){
			employees[lastEmployeeChanged].workedHours += lastTimeCard;
		}
	
		else if(lastUndo == 4){
			employees[lastEmployeeChanged].sales[employees[lastEmployeeChanged].saleNumber] = lastDeletedSale;
			employees[lastEmployeeChanged].saleNumber++;
		}
		
		else if(lastUndo == 5){
			employees[lastEmployeeChanged].serviceTax += lastServiceTax;
		}
		
		else if(lastUndo == 6){
			undoEdit();
		}
		
		else if(lastUndo == 7){
			System.out.println("The employees have already been paid, payroll runs can't be undone or redone!");
		}
		
		else if(lastUndo == 10){
			auxInt = employees[lastEmployeeChanged].paymentFrequency; 
			employees[lastEmployeeChanged].paymentFrequency = lastEditedInt;
			lastEditedInt = auxInt;
			
			auxInt = employees[lastEmployeeChanged].weekPayment;
			employees[lastEmployeeChanged].weekPayment = lastAgendaOption;
			lastAgendaOption = auxInt;
			if(lastEdited == 2){
				auxInt = employees[lastEmployeeChanged].lastPayment;
				employees[lastEmployeeChanged].lastPayment = lastAgendaPaymentOption;
				lastAgendaPaymentOption = auxInt;
			}	
		}

	}
	
	public static void createNewAgenda(){
		
		if(activeEmployeeCount == 0){
			System.out.println("Add at least one employee!\n");
			return;
		}
		
		printCurrentEmployees(0);
		
		int employeeId = idValidation();
		int agenda = 0;
		while(agenda > 2 || agenda < 1){
			System.out.println("What kind of agenda do you want: ");
			System.out.println("  1 - Monthly Based");
			System.out.println("  2 - Weekly Based");
			agenda = scan.nextInt();
			if(agenda > 2 || agenda < 1) System.out.println("Entry not valid, try again.\n");
		}
		lastEdited = agenda;
		lastEditedInt = employees[employeeId].paymentFrequency;
		if(agenda == 1){
			employees[employeeId].paymentFrequency = 2;
			System.out.println("What day of the month will "+employees[employeeId].name+" be paid on?\n ($ for last working day of month)");
			String answer = scan.nextLine();
			auxInt = employees[employeeId].monthPayment;
			if(answer != "$") employees[employeeId].monthPayment = Integer.parseInt(answer);
			else employees[employeeId].monthPayment = 0;
		}
		else{
			System.out.println("Payment Frequency: ");
			System.out.println("  1 - Weekly");
			System.out.println("  2 - Biweekly");
			
			if(scan.nextInt() == 1) employees[employeeId].paymentFrequency = 1;
			else employees[employeeId].paymentFrequency = 3;
			System.out.println("Week day of payment(from 1 - sun to 7 - sat): ");
			
			lastAgendaOption = employees[employeeId].weekPayment;
			lastAgendaPaymentOption = employees[employeeId].lastPayment;
			
			employees[employeeId].weekPayment = scan.nextInt();
			employees[employeeId].lastPayment = 0;
		
		}
		return;
	}
	
	public static int getDate(){
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getWeekDay(){
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getLastDayOfMonth(){
		return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static void setMonth(){
		Calendar date = Calendar.getInstance();
		lastMonth = date.get(Calendar.MONTH) +1;
		lastDayOfLastMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static int getMonth(){
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.MONTH) +1;
	}
	
	public static int getYear(){
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.YEAR);
	}
	
	public static void setLastPayment(){
		lastPayment = getDate()*getMonth()*getYear();
	}
	
	public static int getToday(){
		return getDate()*getMonth()*getYear();
	}
	
	public static int secondSelectDayOfMonth(int weekDay, Employee employee){
		
		int today = getDate();
		int firstDay = 1;
		int firstDayWeek = getWeekDayOfFirstDayOfMonth();
		int paymentDay = firstDay + (weekDay - firstDayWeek)+7;;
		if(employee.lastPayment != 0 && lastMonth != getMonth()){
			paymentDay = 14 + employee.lastPayment;
			if(paymentDay > lastDayOfLastMonth){
				paymentDay -= lastDayOfLastMonth;
			}
		}
		else if(today > paymentDay){
			paymentDay += 14;
		}
		
		return paymentDay;
		
	}
	
	public static int secondFridayOfMonth(){
		
		int today = getDate();
		int firstDay = 1;
		int firstDayWeek = getWeekDayOfFirstDayOfMonth();
		int secondFriday = firstDay + (6 - firstDayWeek)+7;;
		if(lastBiweeklyPayment != 0 && lastMonth != getMonth()){
			secondFriday = 14 + lastBiweeklyPayment;
			if(secondFriday > lastDayOfLastMonth){
				secondFriday -= lastDayOfLastMonth;
			}
		}
		else if(today > secondFriday){
			secondFriday += 14;
		}
		
		return secondFriday;
		
	}
	
	public static int getWeekDayOfFirstDayOfMonth(){
		Calendar firstDay = Calendar.getInstance();
		firstDay.set(Calendar.DATE, firstDay.getActualMinimum(Calendar.DATE));
		return firstDay.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getWeekDayOfLastDayOfMonth(){
		Calendar lastDay = Calendar.getInstance();
		lastDay.set(Calendar.DATE, lastDay.getActualMaximum(Calendar.DATE));
		return lastDay.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int lastWorkingDayOfMonth(){

		int lastDayOfMonth = getLastDayOfMonth();
		int lastDayOfMonthInWeek = getWeekDayOfLastDayOfMonth();
		
		if(lastDayOfMonthInWeek == 7){
			lastDayOfMonthInWeek = 6;
			lastDayOfMonth -= 1;
		}
		else if(lastDayOfMonthInWeek == 1){
			lastDayOfMonthInWeek = 6;
			lastDayOfMonth -= 2;
		}
		return lastDayOfMonth;
	}
	
	public static String typeConversion(int type){
				if(type == 1) return "Hourly";
				else if(type == 2) return "Salaried";
				else return "Comissioned";
	}
	
	public static String paymentRate(int type){
		if(type == 1) return "Hourly";
		else if(type == 2) return "Monthly";
		else return "Bi-weekly";
	}
	
	public static String paymentMethodConversion(int method){
		if(method == 1) return "Mailed Check";
		else if(method == 2) return "Hand-delivered Check";
		else return "Bank Deposit";
	}
	
	public static void printCurrentEmployees(int type){//types: 0 - default, 1 - time card, 2 - sale result, 3 - service tax, 4 - print all employees

		if(type != 4) System.out.println("\nEligible Employees List: ");
		
		else System.out.println("\nEmployee List: ");
		
			if(type == 0){
				for(int i = 0; i < employeeCount; i++){
					System.out.print("  ID: ");
					System.out.printf("%03d", employees[i].id);
					System.out.println(" | Name: "+employees[i].name);
				}
			}
			else if(type == 1){
				for(int i = 0; i < employeeCount; i++){
					if(employees[i].type == 1){
						System.out.print("  ID: ");
						System.out.printf("%03d", employees[i].id);
						System.out.println(" | Name: "+employees[i].name);
					}
				}
			}
			else if(type == 2){
				for(int i = 0; i < employeeCount; i++){
					if(employees[i].type == 3){
						System.out.print("  ID: ");
						System.out.printf("%03d", employees[i].id);
						System.out.println(" | Name: "+employees[i].name);
					}
				}
			}
			else if(type == 4){
				for(int i = 0; i < employeeCount; i++){
					
					System.out.println("\n\t  | Name: "+employees[i].name+"\n\t  | Address: "+employees[i].address);
					System.out.print("  ID: ");
					System.out.printf("%03d", employees[i].id);
					System.out.println(" | Type: "+typeConversion(employees[i].type)+"\n\t  | Salary: R$ "+employees[i].salary+" "+paymentRate(employees[i].paymentFrequency)+"\n\t  | Payment Method: "+paymentMethodConversion(employees[i].paymentMethod)+"\n");
					
				}
			}
			else{
				for(int i = 0; i < employeeCount; i++){
					if(employees[i].syndicateMember == true){
						System.out.print("  ID: ");
						System.out.printf("%03d", employees[i].id);
						System.out.println(" | Name: "+employees[i].name);
					}
				}
			}
		
		return;
		
	}
	
	public static int idValidation(){
		int id;
		System.out.print("\nEmployee's id: ");
		id = scan.nextInt();
		while(id > employeeCount-1 || id < 0){
			System.out.println("Employee id not valid, try again.\n");
			System.out.print("Employee's id: ");
			id = scan.nextInt();
		}
		lastEmployeeChanged = id;
		return id;
	}

	public static void defaultEmployees(){
		Employee newEmployee = new Employee();
		
		newEmployee.id = 0;
		newEmployee.active = true;
		newEmployee.name = "Will Beaumont";
		newEmployee.address = "884 Ashton Lane";
		newEmployee.type = 1;
		newEmployee.paymentFrequency = newEmployee.type;
		newEmployee.salary = 17.85;
		newEmployee.paymentMethod = 2;
		newEmployee.syndicateMember = true;
		newEmployee.syndicateId = 0;
		newEmployee.syndicateTax = 17.85*2;
		employees[0] = newEmployee;
		
		newEmployee = new Employee();
		newEmployee.id = 1;
		newEmployee.active = true;
		newEmployee.name = "Drew Reynolds";
		newEmployee.address = "873 Hog Camp Road";
		newEmployee.type = 1;
		newEmployee.paymentFrequency = newEmployee.type;
		newEmployee.salary = 23.45;
		newEmployee.paymentMethod = 1;
		newEmployee.syndicateMember = false;
		employees[1] = newEmployee;
		
		newEmployee = new Employee();
		newEmployee.id = 2;
		newEmployee.active = true;
		newEmployee.name = "August Richard";
		newEmployee.address = "4914 Woodland Terrace";
		newEmployee.type = 1;
		newEmployee.paymentFrequency = newEmployee.type;
		newEmployee.salary = 13.65;
		newEmployee.paymentMethod = 1;
		newEmployee.syndicateMember = true;
		newEmployee.syndicateId = 1;
		newEmployee.syndicateTax = 13.65*2;
		employees[2] = newEmployee;
		
		newEmployee = new Employee();
		newEmployee.id = 3;
		newEmployee.active = true;
		newEmployee.name = "Jace Patrickson";
		newEmployee.address = "2945 Park Street";
		newEmployee.type = 2;
		newEmployee.paymentFrequency = newEmployee.type;
		newEmployee.salary = 3225.75;
		newEmployee.paymentMethod = 2;
		newEmployee.syndicateMember = false;
		employees[3] = newEmployee;
		
		newEmployee = new Employee();
		newEmployee.id = 4;
		newEmployee.active = true;
		newEmployee.name = "Josh Fluttershy";
		newEmployee.address = "4253 Wascana Parkway";
		newEmployee.type = 2;
		newEmployee.paymentFrequency = newEmployee.type;
		newEmployee.salary = 1914.50;
		newEmployee.paymentMethod = 3;
		newEmployee.syndicateMember = false;
		employees[4] = newEmployee;
		
		newEmployee = new Employee();
		newEmployee.id = 5;
		newEmployee.active = true;
		newEmployee.name = "Jimmy Roe";
		newEmployee.address = "3283 Churchill Plaza";
		newEmployee.type = 1;
		newEmployee.paymentFrequency = newEmployee.type;
		newEmployee.salary = 14.25;
		newEmployee.paymentMethod = 1;
		newEmployee.syndicateMember = false;
		employees[5] = newEmployee;
		
		newEmployee = new Employee();
		newEmployee.id = 6;
		newEmployee.active = true;
		newEmployee.name = "Norbert Abrams";
		newEmployee.address = "2586 Glover Road";
		newEmployee.type = 3;
		newEmployee.paymentFrequency = newEmployee.type;
		newEmployee.commission = 8.0;
		newEmployee.salary = 1636.30/2;
		newEmployee.paymentMethod = 3;
		newEmployee.syndicateMember = true;
		newEmployee.syndicateId = 2;
		newEmployee.syndicateTax = 54.75;
		employees[6] = newEmployee;
		
		newEmployee = new Employee();
		newEmployee.id = 7;
		newEmployee.active = true;
		newEmployee.name = "Jerrod Arkwright";
		newEmployee.address = "81 Thompsons Lane";
		newEmployee.type = 3;
		newEmployee.paymentFrequency = newEmployee.type;
		newEmployee.commission = 12.5;
		newEmployee.salary = 2183.50/2;
		newEmployee.paymentMethod = 2;
		newEmployee.syndicateMember = true;
		newEmployee.syndicateId = 3;
		newEmployee.syndicateTax = 72.50;
		employees[7] = newEmployee;
		
		newEmployee = new Employee();
		newEmployee.id = 8;
		newEmployee.active = true;
		newEmployee.name = "Adam Emmett";
		newEmployee.address = "47 Berkeley Road";
		newEmployee.type = 1;
		newEmployee.paymentFrequency = newEmployee.type;
		newEmployee.salary = 12.75;
		newEmployee.paymentMethod = 3;
		newEmployee.syndicateMember = true;
		newEmployee.syndicateId = 4;
		newEmployee.syndicateTax = 12.75*2;
		employees[8] = newEmployee;
		
		newEmployee = new Employee();
		newEmployee.id = 9;
		newEmployee.active = true;
		newEmployee.name = "Maynard Devin";
		newEmployee.address = "56 Station Road";
		newEmployee.type = 2;
		newEmployee.paymentFrequency = newEmployee.type;
		newEmployee.salary = 1864.75;
		newEmployee.paymentMethod = 2;
		newEmployee.syndicateMember = false;
		employees[9] = newEmployee;
		
		employeeCount = 10;
		activeEmployeeCount = 10;
	}
	
	public static void editSyndicateOptions(int employeeId){
		int answer;
		lastEditedSyndicateStatus = employees[employeeId].syndicateMember;
		lastEditedSyndicateTax = employees[employeeId].syndicateTax;
		if(employees[employeeId].syndicateMember == false){
			while(true){
				System.out.println("Is this employee joining the syndicate member?");
				System.out.println("  1 - Yes");
				System.out.println("  2 - No");
				answer = scan.nextInt();
				if(answer == 1){
					employees[employeeId].syndicateMember = true;
					employees[employeeId].syndicateId = syndicateMembers;
					syndicateMembers++;
					System.out.print("Syndicate Tax: R$ ");
					employees[employeeId].syndicateTax = scan.nextDouble();
					break;
				}
				else if(answer == 2){
					break;
				}
				else System.out.println("Invalid option, try again.\n");
			}
		}
		else if(employees[employeeId].syndicateMember == true){
			while(true){
				System.out.println("Do you wish to change this employee's syndicate tax?");
				System.out.println("  1 - Yes");
				System.out.println("  2 - No");
				answer = scan.nextInt();
				if(answer == 1){
					System.out.println("Current Syndicate Tax: R$ "+employees[employeeId].syndicateTax);
					System.out.print("New Syndicate Tax: R$ ");
					employees[employeeId].syndicateTax = scan.nextDouble();
					break;
				}
				else if(answer == 2){
					while(true){
						System.out.println("Is this employee leaving the syndicate?");
						System.out.println("  1 - Yes");
						System.out.println("  2 - No");
						answer = scan.nextInt();
						if(answer == 1){
							employees[employeeId].syndicateMember = false;
							employees[employeeId].syndicateId = -1;
							employees[employeeId].syndicateTax = 0.0;
							break;
						}
						else if(answer == 2){
							break;
						}
						else System.out.println("Invalid option, try again.\n");
					}
					break;
				}
				else System.out.println("Invalid option, try again.\n");
			}

		}
		return;
		
	}
}
