package kz.one_c_rating.osgi;

/**
 * Created by Администратор on 17.05.2017.
 */
public class SampleResult {
    private String greeting;

    public SampleResult(String name) {
        this.greeting = "Hello " + name;
    }

    public String getGreeting() {

        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}

