```java name=src/test/java/com/vaadin/starter/bakery/test/FormattingTest.java url=https://github.com/LETI-106804/bakery-app-starter-flow-spring/blob/dea86690167b7947892badf6205c6e62e36dbfd7/src/test/java/com/vaadin/starter/bakery/test/FormattingTest.java
package com.vaadin.starter.bakery.test;

import java.util.Locale;
import java.util.Locale.Category;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Test superclass that runs tests in Turkish locale.
 * 
 * This abstract test class sets the default locale for formatting to Turkish ("tr", "TR")
 * before any tests in derived classes are run, and restores the original locale 
 * after all tests have completed. This ensures consistent locale-specific 
 * formatting behavior during tests, which is useful for verifying internationalization 
 * and localization code.
 */
public abstract class FormattingTest {

    // Stores the original default locale for formatting so it can be restored after testing.
	private static Locale locale;

    /**
     * Sets up the test class by changing the default formatting locale to Turkish.
     * This method is executed once before all tests in the class.
     */
	@BeforeAll
	public static void setUpClass() {
        // Save the current default FORMAT locale.
		locale = Locale.getDefault(Category.FORMAT);
        // Set the default FORMAT locale to Turkish.
		Locale.setDefault(Category.FORMAT, new Locale("tr", "TR"));
	}

    /**
     * Tears down the test class by restoring the original default formatting locale.
     * This method is executed once after all tests in the class.
     */
	@AfterAll
	public static void tearDownClass() {
        // Restore the original FORMAT locale.
		Locale.setDefault(Category.FORMAT, locale);
        // Clear the saved locale reference.
		locale = null;
	}

}
```
