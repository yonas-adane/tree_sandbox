import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

class from {
    public String uri;
}

class log {
    public String message;
}

class to {
    public String uri;
}

class bean {
    public String ref;
    public String method;
}

class when {
    public String simple;
    public bean bean;
    public to to;
}

class choice {
    @JacksonXmlElementWrapper(useWrapping = false)
    public when[] when;
}

public class route {
    public from from;
    public log log;
    public choice choice;
    public to to;
    public String id;
}

