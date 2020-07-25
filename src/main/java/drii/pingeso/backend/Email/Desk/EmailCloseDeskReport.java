package drii.pingeso.backend.Email.Desk;

public class EmailCloseDeskReport {

    private Report report;

    public EmailCloseDeskReport(Report report) {
        this.report = report;
    }

    public String getVoucher(){
        String message =
                        "<p>Encargado de caja: &nbsp; "+ report.getUserName() +"</p>" +
                        "<p><span style=\"text-decoration: underline;\">Ingresos esperados:</span></p>" +
                        "<p><span style=\"text-decoration: underline;\">Ingresos capturados:</span></p>" +
                        /*"<ul>" +
                        "<li>Efectivo:  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getEfectivoCapturado()+"</li>" +
                        "<li>D&eacute;bito: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getDebitoCapturado()+"</li>" +
                        "<li>Cr&eacute;dito: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getCreditoCapturado()+"</li>" +
                        "<li>Transferencias: &nbsp; $"+report.getTransferenciaCapturado()+"</li>" +
                        "</ul>" +*/
                        "<p><span style=\"text-decoration: underline;\">Diferencia:</span></p>" +
                        "<p><span style=\"text-decoration: underline;\">Total:</span></p>";
        return message;
    }
}