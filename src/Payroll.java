import java.util.Calendar;
import java.util.Scanner;

public class Payroll {
	
	static Scanner scan = new Scanner(System.in);
	static int syndicateMembers = 0;
	static int employeeCount = 0;
	static Employee[] employees = new Employee[200];
	static int lastAction = 0;
	static int lastMonth = 0;
	static int lastPayment = 0;
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
				System.out.println("  10 - Close \n");
				menuOption = scan.nextInt();
			}
			
			if((lastAction == 2 && menuOption != 8) || menuOption != 9) deleteEmployee();
			if(lastAction == menuOption && menuOption == 8) System.out.println("You can't undo twice in a row");

			if(menuOption == 1){
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
				if(lastAction == 8) System.out.println("You can't undo twice, try using redo instead.");
				else undo();				
			}
			
			else if(menuOption == 9){
				if(lastAction == 9) System.out.println("You can't redo twice, try using undo instead.");
				else redo();				
			}
			
			else if(menuOption != 10){
				System.out.println("Function still not implemented");	
			}
			
			lastAction = menuOption;
		
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
				newEmployee.paymentFrequency = newEmployee.type;
			}
			
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
		printCurrentEmployees();
		
		int removeId = idValidation();
		
		removeActive(removeId);
		System.out.println("Employee removed successfully!\n");
		return;
		
	}
	
	public static void removeActive(int id){ //for deleting purposes
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
		
		printCurrentEmployees();
		
		int employeeId = idValidation();
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
		
		printCurrentEmployees();
		
		int employeeId = idValidation();
		
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
		
		printCurrentEmployees();
		
		int employeeId = idValidation();
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
		
		printCurrentEmployees();
		
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
			if(entry > 8 || entry < 1) System.out.println("Entry not valid, try again.\n");
		}
		entry = lastEdited;
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
		
		if(today == weeklyPayment){
			
			for(int i = 0; i < employeeCount; i++){
				if(employees[i].active == true){
					if(employees[i].paymentFrequency == 1){
						System.out.println("Paid "+employees[i].name+": R$ "+calculatePayCheck(employees[i])+" via "+paymentMethodConversion(employees[i].paymentMethod));
					}
				}	
			}	
		}
		if(today == monthlyPaymentDay){
			
			for(int i = 0; i < employeeCount; i++){
				if(employees[i].active == true){
					if(employees[i].paymentFrequency == 2){
						System.out.println("Paid "+employees[i].name+": R$ "+calculatePayCheck(employees[i])+" via "+paymentMethodConversion(employees[i].paymentMethod));
					}
				}	
			}	
		}
		if(today == biweeklyPaymentDay){
			
			for(int i = 0; i < employeeCount; i++){
				if(employees[i].active == true){
					if(employees[i].paymentFrequency == 3){
						System.out.println("Paid "+employees[i].name+": R$ "+calculatePayCheck(employees[i])+" via "+paymentMethodConversion(employees[i].paymentMethod));
					}
				}	
			}	
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
	
	public static void undo(){
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
		
		lastUndo = lastAction;
		
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
	}
	
	public static int getMonth(){
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.MONTH) +1;
	}
	
	public static int getYear(){
		Calendar date = Calendar.getInstance();
		System.out.println(date.get(Calendar.YEAR));
		return date.get(Calendar.YEAR);
	}
	
	public static void setLastPayment(){
		lastPayment = getDate()*getMonth()*getYear();
	}
	
	public static int getToday(){
		return getDate()*getMonth()*getYear();
	}
	
	public static int secondFridayOfMonth(){
		
		int firstDay = 1;
		int firstDayWeek = firstDayOfMonthInWeekDays();
		int secondFriday = firstDay + (6 - firstDayWeek)+6;
		return secondFriday;
		
	}
	
	public static int firstDayOfMonthInWeekDays(){
	
		int dayOfWeek = getWeekDay();
		int dayOfMonth = getDate();
		int firstDayOfMonth = 1;
		int firstDayOfMonthInWeekDays = dayOfWeek;
		
		firstDayOfMonthInWeekDays -= (dayOfMonth - firstDayOfMonth) % 7;
		firstDayOfMonthInWeekDays %= 7;
		System.out.println(firstDayOfMonthInWeekDays);
		return firstDayOfMonthInWeekDays;
		
	}
	
	public static int lastWorkingDayOfMonth(){
		
		int dayOfWeek = getWeekDay();
		int dayOfMonth = getDate();
		int lastDayOfMonth = getLastDayOfMonth();
		int lastDayOfMonthInWeek = dayOfWeek;
		
		lastDayOfMonthInWeek += (lastDayOfMonth - dayOfMonth) % 7;
		lastDayOfMonthInWeek %= 7;
		
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
	
	public static void printCurrentEmployees(){
		
		System.out.println("\nEmployee List: ");
		for(int i = 0; i < employeeCount; i++){
			System.out.print("  ID: ");
			if(employees[i].id < 10) System.out.print("0");
			if(employees[i].id < 100) System.out.print("0");
			System.out.println(employees[i].id+" | Name: "+employees[i].name);
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
