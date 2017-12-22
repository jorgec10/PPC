package Calculator;

public class Response {

    private double result;

    public Response(double result) {
        this.result = result;
    }

    public double getResult() {
        return result;
    }

    public String toXml() {
        return  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<response>\n" +
                "   <result>" + result + "</result>\n" +
                "</response>";
    }

    public String toJson() {
        return  "{\n" +
                "   \"result\":\"" + result + "\"\n" +
                "}";
    }
}
