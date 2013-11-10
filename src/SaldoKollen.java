
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;	

/**
* SaldoKollen - Handelsbanken
* 
* Allows users to login to their Handelsbanken account to check balance and latest transactions. 
* The user logs in with social security number and pre-set PIN code.
*  
* @author Markus Östberg
* @version 1.0
*/

public class SaldoKollen {

	private static WebDriver driver;
	
	public static void main(String[] args) {
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.WARNING);
		
        driver = new HtmlUnitDriver();
        driver.get("https://m.handelsbanken.se/");
        System.out.println("Welcome to SaldoKollen. Log in to check your balance at Handelsbanken");
        
        login();
        
        ArrayList<String> accounts = getAccountNames();
        
        for(String accout : accounts){
        	printTransactions(accout);
        }
        back();
        logout();
	}
	
	private static void login(){
        WebElement logInLinkElement = driver.findElement(By.linkText("Logga in"));
        logInLinkElement.click();
        
        loginProcedure();
	}
	
	private static void loginProcedure(){
		String userName;
        String userPin;
        
        //Read user data from console
        Scanner in = new Scanner(System.in);
        System.out.println("Enter username: [YYMMDDXXXX]");
        userName = in.nextLine();
        System.out.println("Enter pin: [XXXX]");
        userPin = in.nextLine();
        
        WebElement userNameElement = driver.findElement(By.name("username"));
        userNameElement.sendKeys(userName);
        
        WebElement userPinElement = driver.findElement(By.name("pin"));
        userPinElement.sendKeys(userPin);
        
        WebElement loginButtonElement = driver.findElement(By.name("execute"));
        loginButtonElement.submit();
        
        //Check if logged in
        WebElement bodyTag = driver.findElement(By.tagName("body")); 
        if (bodyTag.getText().contains("Kontrollera dina uppgifter och försök igen.")){
        	System.out.println("Login failed, try again!");
        	loginProcedure();
        }
	}
	
	private static ArrayList<String> getAccountNames(){
		ArrayList<String> accountNames = new ArrayList<String>();
		
        WebElement accountsLinkElement = driver.findElement(By.linkText("Konton"));
        accountsLinkElement.click();
        
        List<WebElement> accounts = driver.findElements(By.xpath("//ul[@class='link-list']/li[@class!='link-list-header']/a/span[not(@*)]"));
         
        for(WebElement account: accounts)
        	accountNames.add(account.getText());

		return accountNames;
	}
	
	private static void printTransactions(String accoutName){
		WebElement account = driver.findElement(By.partialLinkText(accoutName));
    	account.click();

    	//Print Account balance and transactions
    	System.out.println("____________________" + accoutName + "____________________");
    	System.out.println("Balance: " + driver.findElement(By.xpath("//div[@class='pillow']/span/h2[position()=2]")).getText());
    	System.out.println("_______________Transactions_____________________");
    	List<WebElement> transactions = driver.findElements(By.xpath("//ul[@class='link-list']/li"));
    	for(WebElement transaction: transactions)
    		System.out.println(transaction.getText());
    	System.out.println("_________________________________________________");
    	back();
	}
	
	private static void back(){
		WebElement backLinkElement = driver.findElement(By.linkText("Tillbaka"));
        backLinkElement.click();
	}

	
	private static void logout(){
        WebElement logoutElement = driver.findElement(By.linkText("Logga ut"));
        logoutElement.click();
        System.out.println("Thanks for using SaldoKollen. You are now logged off!");
	}
}
