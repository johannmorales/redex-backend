package org.redex.backend.zelper.pdf;

import org.redex.backend.zelper.email.MailEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.thymeleaf.templatemode.TemplateMode.HTML;

@Service
public class PdfContentBuilder {

    private TemplateEngine templateEngine;

    private static final String UTF_8 = "UTF-8";

    @Autowired
    public PdfContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/pdf/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    private Data exampleDataForJohnDoe() {
        Data data = new Data();
        data.setFirstname("John");
        data.setLastname("Doe");
        data.setStreet("Example Street 1");
        data.setZipCode("12345");
        data.setCity("Example City");
        return data;
    }

    static class Data {
        private String firstname;
        private String lastname;
        private String street;
        private String zipCode;
        private String city;

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

    public String build(PdfEnum pdf, Context ctx) {
        Data data = exampleDataForJohnDoe();

        Context context = new Context();
        context.setVariable("data", data);

        return templateEngine.process( pdf.getTemplate(), context);
    }
}
