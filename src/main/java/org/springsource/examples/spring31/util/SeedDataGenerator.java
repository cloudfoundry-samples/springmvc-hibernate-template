package org.springsource.examples.spring31.util;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Josh Long
 */
public class SeedDataGenerator implements InitializingBean {

    private String names = "Juergen Hoeller, Mark Fisher, Rod Johnson, David Syer, Gunnar Hillert, Dave McCrory, Josh Long, Patrick Chanezon, Andy Piper, Eric Bottard, Chris Richardson, Raja Rao, Rajdeep Dua, Monica Wilkinson, Mark Pollack";

    private Set<String[]> orderedUniqueNames = new LinkedHashSet<String[]>();

    private String h2SqlTemplate = "INSERT INTO customer(firstname, lastname, signupdate) values ('%s','%s', NOW() );";

    private String postgreSqlTemplate = "INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , '%s', '%s', NOW());";

    public void setNames(String n) {
        this.names = n;
    }

    public void addName(String f, String l) {
        this.orderedUniqueNames.add(new String[]{f, l});
    }

    public void generate(Writer writer, String sqlTemplate) throws Exception {

        for (String[] nameParts : this.orderedUniqueNames) {
            writer.write(String.format(sqlTemplate, nameParts[0], nameParts[1]));
            writer.write(System.getProperty("line.separator"));
        }
    }

    public void generate() throws Exception {
        String[] templates = {h2SqlTemplate, postgreSqlTemplate};
        for (String t : templates) {
            StringWriter stringWriter = new StringWriter();
            generate(stringWriter, t);
            String sql = stringWriter.getBuffer().toString();
            System.out.println("--" + t);
            System.out.println(sql);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert StringUtils.hasText(this.names) : "the names property should have a comma delimited list of first and last name pairs, like this 'Josh Long, Mark Fisher, ...'";
        String[] nameArr = names.split(",");
        assert nameArr.length > 0 : "you must specify values for the names";
        for (String noms : nameArr) {
            String[] nameParts = noms.trim().split(" ");
            nameParts[0] = nameParts[0].trim();
            nameParts[1] = nameParts[1].trim();
            addName(nameParts[0], nameParts[1]);
        }
    }

    public static void main(String[] args) throws Exception {
        SeedDataGenerator seedDataGenerator = new SeedDataGenerator();
        seedDataGenerator.afterPropertiesSet();
        seedDataGenerator.generate();
    }
}
