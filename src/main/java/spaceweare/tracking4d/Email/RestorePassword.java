package spaceweare.tracking4d.Email;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestorePassword {


    public static String createEmailContent(String temporalPassword, String name, String lastName){
        String emailContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html style=\"width:100%;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0;\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\n" +
                "    <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta content=\"telephone=no\" name=\"format-detection\">\n" +
                "    <title>Nuevo correo electrónico</title>\n" +
                "    <!--[if (mso 16)]>\n" +
                "      <style type=\"text/css\">\n" +
                "        a {text-decoration: none;}\n" +
                "      </style>\n" +
                "    <![endif]-->\n" +
                "    <!--[if gte mso 9]><style>\n" +
                "        sup { font-size: 100% !important; }\n" +
                "      </style><![endif]-->\n" +
                "    <style>\n" +
                "      .es-button-border:hover a.es-button{background:#ffffff !important;border-color:#ffffff !important}\n" +
                "      .es-button-border:hover{background:#ffffff !important;border-style:solid solid solid solid !important;border-color:#3d5ca3 #3d5ca3 #3d5ca3 #3d5ca3 !important}\n" +
                "      td .es-button-border:hover a.es-button-1{background:#aNaNaN !important;border-color:#aNaNaN !important}\n" +
                "      td .es-button-border-2:hover{background:#aNaNaN !important;border-color:#556e80 #556e80 #556e80 #556e80 !important}\n" +
                "    </style>\n" +
                "    <style type=\"text/css\">\n" +
                "      @media only screen and (max-width:600px) {p, ul li, ol li, a { font-size:16px!important; line-height:150%!important } h1 { font-size:20px!important; text-align:center; line-height:120%!important } h2 { font-size:16px!important; text-align:left; line-height:120%!important } h3 { font-size:20px!important; text-align:center; line-height:120%!important } h1 a { font-size:20px!important } h2 a { font-size:16px!important; text-align:left } h3 a { font-size:20px!important } .es-menu td a { font-size:14px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:10px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:12px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button { font-size:14px!important; display:block!important; border-left-width:0px!important; border-right-width:0px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } .es-desk-hidden { display:table-row!important; width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } .es-desk-menu-hidden { display:table-cell!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }\n" +
                "      .ExternalClass {\n" +
                "      \twidth:100%;\n" +
                "      }\n" +
                "      .ExternalClass,\n" +
                "      .ExternalClass p,\n" +
                "      .ExternalClass span,\n" +
                "      .ExternalClass font,\n" +
                "      .ExternalClass td,\n" +
                "      .ExternalClass div {\n" +
                "      \tline-height:100%;\n" +
                "      }\n" +
                "      a[x-apple-data-detectors] {\n" +
                "      \tcolor:inherit!important;\n" +
                "      \ttext-decoration:none!important;\n" +
                "      \tfont-size:inherit!important;\n" +
                "      \tfont-family:inherit!important;\n" +
                "      \tfont-weight:inherit!important;\n" +
                "      \tline-height:inherit!important;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body style=\"width:100%;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0;\">\n" +
                "    <div class=\"es-wrapper-color\" style=\"background-color:#FAFAFA;\">\n" +
                "      <!--[if gte mso 9]>\n" +
                "        <v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n" +
                "          <v:fill type=\"tile\" color=\"#fafafa\"></v:fill>\n" +
                "        </v:background>\n" +
                "      <![endif]-->\n" +
                "      <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top;\">\n" +
                "        <tr style=\"border-collapse:collapse;\">\n" +
                "          <td valign=\"top\" style=\"padding:0;Margin:0;\">\n" +
                "            <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\">\n" +
                "              <tr style=\"border-collapse:collapse;\">\n" +
                "                <td class=\"es-adaptive\" align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                  <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\">\n" +
                "                    <tr style=\"border-collapse:collapse;\">\n" +
                "                      <td align=\"left\" style=\"padding:10px;Margin:0;\">\n" +
                "                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                          <tr style=\"border-collapse:collapse;\">\n" +
                "                            <td width=\"580\" valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                              <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td class=\"es-infoblock\" align=\"center\" style=\"padding:0;Margin:0;line-height:14px;font-size:12px;color:#CCCCCC;\">\n" +
                "                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:12px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:14px;color:#CCCCCC;\">Recuperaci&oacute;n de contrase&ntilde;a<a href=\"https://viewstripo.email\" class=\"view\" target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-size:12px;text-decoration:none;color:#CCCCCC;\">View in browser</a></p>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </table>\n" +
                "            <table class=\"es-header\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top;\">\n" +
                "              <tr style=\"border-collapse:collapse;\">\n" +
                "                <td class=\"es-adaptive\" align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                  <table class=\"es-header-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#3D5CA3;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#3d5ca3\" align=\"center\">\n" +
                "                    <tr style=\"border-collapse:collapse;\">\n" +
                "                      <td style=\"Margin:0;padding-top:20px;padding-bottom:20px;padding-left:20px;padding-right:20px;background-color:#FE5000;background-position:left top;\" bgcolor=\"#fe5000\" align=\"left\">\n" +
                "                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                          <tr style=\"border-collapse:collapse;\">\n" +
                "                            <td width=\"560\" align=\"left\" style=\"padding:0;Margin:0;\">\n" +
                "                              <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"transparent\">\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td style=\"padding:0;Margin:0;display:none;\" align=\"center\"></td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </table>\n" +
                "            <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\">\n" +
                "              <tr style=\"border-collapse:collapse;\">\n" +
                "                <td style=\"padding:0;Margin:0;background-color:#FAFAFA;\" bgcolor=\"#fafafa\" align=\"center\">\n" +
                "                  <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\">\n" +
                "                    <tr style=\"border-collapse:collapse;\">\n" +
                "                      <td style=\"padding:0;Margin:0;padding-left:20px;padding-right:20px;padding-top:40px;background-color:transparent;background-position:left top;\" bgcolor=\"transparent\" align=\"left\">\n" +
                "                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                          <tr style=\"border-collapse:collapse;\">\n" +
                "                            <td width=\"560\" valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                              <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-position:left top;\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"center\" style=\"padding:0;Margin:0;padding-top:5px;padding-bottom:5px;\"><img src=\"https://fkdgyh.stripocdn.email/content/guids/CABINET_706f535f1f3cf77fd2e02e50d964aced/images/26371580157980545.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;\" width=\"175\"></td>\n" +
                "                                </tr>\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"center\" style=\"padding:0;Margin:0;padding-top:15px;padding-bottom:15px;\">\n" +
                "                                    <h1 style=\"Margin:0;line-height:24px;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-size:20px;font-style:normal;font-weight:normal;color:#333333;\"><strong>¿OLVIDASTE TU</strong></h1>\n" +
                "                                    <h1 style=\"Margin:0;line-height:24px;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-size:20px;font-style:normal;font-weight:normal;color:#333333;\"><strong>&nbsp;CONTRASE&Ntilde;A?</strong></h1>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"left\" style=\"padding:0;Margin:0;padding-left:40px;padding-right:40px;\">\n" +
                "                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:16px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:24px;color:#666666;text-align:center;\">Hola,&nbsp;" + name + " " + lastName + "</p>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"left\" style=\"padding:0;Margin:0;padding-right:35px;padding-left:40px;\">\n" +
                "                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:16px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:24px;color:#666666;text-align:center;\">Esta es una solicitud de cambio de contrase&ntilde;a</p>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"center\" style=\"padding:0;Margin:0;padding-top:25px;padding-left:40px;padding-right:40px;\">\n" +
                "                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:16px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:24px;color:#666666;\">Si tu no hiciste este requerimiento solo ignora este email. En caso contrario presiona el siguiente bot&oacute;n</p>\n" +
                "                                    <p>&nbsp;</p>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"center\" style=\"Margin:0;padding-left:10px;padding-right:10px;padding-top:40px;padding-bottom:40px;\"><span class=\"es-button-border es-button-border-2\" style=\"border-style:solid;border-color:#425563;background:#FFFFFF none repeat scroll 0% 0%;border-width:2px;display:inline-block;border-radius:10px;width:auto;\"><a href=\"http://localhost:8081/autenticacion\" class=\"es-button es-button-1\" target=\"_blank\" style=\"mso-style-priority:100 !important;text-decoration:none !important;mso-style-priority:100 !important;text-decoration:none;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-size:14px;color:#FE5000;border-style:solid;border-color:#FFFFFF;border-width:15px 20px 15px 20px;display:inline-block;background:#FFFFFF none repeat scroll 0% 0%;border-radius:10px;font-weight:bold;font-style:normal;line-height:17px;width:auto;text-align:center;\">RECUPERAR CONTRASEÑA</a></span></td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr style=\"border-collapse:collapse;\">\n" +
                "                      <td style=\"padding:0;Margin:0;padding-left:10px;padding-right:10px;padding-top:20px;background-position:center center;\" align=\"left\">\n" +
                "                        <!--[if mso]>\n" +
                "                          <table width=\"580\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                            <tr>\n" +
                "                              <td width=\"199\" valign=\"top\"><![endif]-->\n" +
                "                        <table class=\"es-left\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;float:left;\">\n" +
                "                          <tr style=\"border-collapse:collapse;\">\n" +
                "                            <td width=\"199\" align=\"left\" style=\"padding:0;Margin:0;\">\n" +
                "                              <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-position:center center;\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td class=\"es-m-txt-c\" align=\"right\" style=\"padding:0;Margin:0;padding-top:15px;\">\n" +
                "                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:16px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:24px;color:#666666;\"><strong>S&iacute;guenos en:</strong></p>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                        <!--[if mso]></td>\n" +
                "                        <td width=\"20\"></td>\n" +
                "                        <td width=\"361\" valign=\"top\"><![endif]-->\n" +
                "                        <table class=\"es-right\" cellspacing=\"0\" cellpadding=\"0\" align=\"right\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;float:right;\">\n" +
                "                          <tr style=\"border-collapse:collapse;\">\n" +
                "                            <td width=\"361\" align=\"left\" style=\"padding:0;Margin:0;\">\n" +
                "                              <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-position:center center;\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td class=\"es-m-txt-c\" align=\"left\" style=\"padding:0;Margin:0;padding-bottom:5px;padding-top:10px;\">\n" +
                "                                    <table class=\"es-table-not-adapt es-social\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                                      <tr style=\"border-collapse:collapse;\">\n" +
                "                                        <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px;\"><img src=\"https://fkdgyh.stripocdn.email/content/assets/img/social-icons/rounded-gray/facebook-rounded-gray.png\" alt=\"Fb\" title=\"Facebook\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;\"></td>\n" +
                "                                        <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px;\"><img src=\"https://fkdgyh.stripocdn.email/content/assets/img/social-icons/rounded-gray/twitter-rounded-gray.png\" alt=\"Tw\" title=\"Twitter\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;\"></td>\n" +
                "                                        <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px;\"><img src=\"https://fkdgyh.stripocdn.email/content/assets/img/social-icons/rounded-gray/instagram-rounded-gray.png\" alt=\"Ig\" title=\"Instagram\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;\"></td>\n" +
                "                                        <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px;\"><img src=\"https://fkdgyh.stripocdn.email/content/assets/img/social-icons/rounded-gray/youtube-rounded-gray.png\" alt=\"Yt\" title=\"Youtube\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;\"></td>\n" +
                "                                        <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px;\"><img src=\"https://fkdgyh.stripocdn.email/content/assets/img/social-icons/rounded-gray/linkedin-rounded-gray.png\" alt=\"In\" title=\"Linkedin\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;\"></td>\n" +
                "                                      </tr>\n" +
                "                                    </table>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                        <!--[if mso]></td>\n" +
                "                      </tr>\n" +
                "                    </table>\n" +
                "                        <![endif]-->\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr style=\"border-collapse:collapse;\">\n" +
                "                      <td style=\"Margin:0;padding-top:5px;padding-bottom:20px;padding-left:20px;padding-right:20px;background-position:left top;\" align=\"left\">\n" +
                "                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                          <tr style=\"border-collapse:collapse;\">\n" +
                "                            <td width=\"560\" valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                              <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:21px;color:#666666;\">Cont&aacute;ctanos: <a target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-size:14px;text-decoration:none;color:#666666;\" href=\"tel:123456789\">123456789</a> | <a target=\"_blank\" href=\"mailto:your@mail.com\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-size:14px;text-decoration:none;color:#666666;\">your@mail.com</a></p>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </table>\n" +
                "            <table class=\"es-footer\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top;\">\n" +
                "              <tr style=\"border-collapse:collapse;\">\n" +
                "                <td style=\"padding:0;Margin:0;background-color:#FAFAFA;\" bgcolor=\"#fafafa\" align=\"center\">\n" +
                "                  <table class=\"es-footer-body\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\">\n" +
                "                    <tr style=\"border-collapse:collapse;\">\n" +
                "                      <td style=\"Margin:0;padding-top:10px;padding-left:20px;padding-right:20px;padding-bottom:30px;background-color:#425563;background-position:left top;\" bgcolor=\"#425563\" align=\"left\">\n" +
                "                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                          <tr style=\"border-collapse:collapse;\">\n" +
                "                            <td width=\"560\" valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                              <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"left\" style=\"padding:0;Margin:0;padding-top:5px;padding-bottom:5px;\">\n" +
                "                                    <h2 style=\"Margin:0;line-height:19px;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-size:16px;font-style:normal;font-weight:normal;color:#FFFFFF;\"><strong>Have quastions?</strong></h2>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:5px;\">\n" +
                "                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:21px;color:#FFFFFF;\">We are here to help, learn more about us <a target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-size:14px;text-decoration:none;color:#FFFFFF;\" href=\"\">here</a></p>\n" +
                "                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:21px;color:#FFFFFF;\">or <a target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-size:14px;text-decoration:none;color:#FFFFFF;\" href=\"\">contact us</a><br>\n" +
                "                                    </p>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </table>\n" +
                "            <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\">\n" +
                "              <tr style=\"border-collapse:collapse;\">\n" +
                "                <td style=\"padding:0;Margin:0;background-color:#FAFAFA;\" bgcolor=\"#fafafa\" align=\"center\">\n" +
                "                  <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"transparent\" align=\"center\">\n" +
                "                    <tr style=\"border-collapse:collapse;\">\n" +
                "                      <td style=\"padding:0;Margin:0;padding-top:15px;background-position:left top;\" align=\"left\">\n" +
                "                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                          <tr style=\"border-collapse:collapse;\">\n" +
                "                            <td width=\"600\" valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                              <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td style=\"padding:0;Margin:0;\">\n" +
                "                                    <table class=\"es-menu\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                                      <tr class=\"links\" style=\"border-collapse:collapse;\">\n" +
                "                                        <td style=\"Margin:0;padding-left:5px;padding-right:5px;padding-top:0px;padding-bottom:1px;border:0;\" id=\"esd-menu-id-0\" width=\"33.33%\" valign=\"top\" bgcolor=\"transparent\" align=\"center\"><a target=\"_blank\" href=\"https://viewstripo.email\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-size:14px;text-decoration:none;display:block;color:#3D5CA3;\">Sing up</a></td>\n" +
                "                                        <td style=\"Margin:0;padding-left:5px;padding-right:5px;padding-top:0px;padding-bottom:1px;border:0;border-left:1px solid #3D5CA3;\" id=\"esd-menu-id-1\" esdev-border-color=\"#3d5ca3\" width=\"33.33%\" valign=\"top\" bgcolor=\"transparent\" align=\"center\"><a target=\"_blank\" href=\"https://viewstripo.email\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-size:14px;text-decoration:none;display:block;color:#3D5CA3;\">Blog</a></td>\n" +
                "                                        <td style=\"Margin:0;padding-left:5px;padding-right:5px;padding-top:0px;padding-bottom:1px;border:0;border-left:1px solid #3D5CA3;\" id=\"esd-menu-id-2\" esdev-border-color=\"#3d5ca3\" width=\"33.33%\" valign=\"top\" bgcolor=\"transparent\" align=\"center\"><a target=\"_blank\" href=\"https://viewstripo.email\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-size:14px;text-decoration:none;display:block;color:#3D5CA3;\">About us</a></td>\n" +
                "                                      </tr>\n" +
                "                                    </table>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"center\" style=\"padding:0;Margin:0;padding-bottom:20px;padding-left:20px;padding-right:20px;\">\n" +
                "                                    <table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                                      <tr style=\"border-collapse:collapse;\">\n" +
                "                                        <td style=\"padding:0;Margin:0px;border-bottom:1px solid #FAFAFA;background:none;height:1px;width:100%;margin:0px;\"></td>\n" +
                "                                      </tr>\n" +
                "                                    </table>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </table>\n" +
                "            <table class=\"es-footer\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top;\">\n" +
                "              <tr style=\"border-collapse:collapse;\">\n" +
                "                <td style=\"padding:0;Margin:0;background-color:#FAFAFA;\" bgcolor=\"#fafafa\" align=\"center\">\n" +
                "                  <table class=\"es-footer-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"transparent\" align=\"center\">\n" +
                "                    <tr style=\"border-collapse:collapse;\">\n" +
                "                      <td align=\"left\" style=\"Margin:0;padding-bottom:5px;padding-top:15px;padding-left:20px;padding-right:20px;\">\n" +
                "                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                          <tr style=\"border-collapse:collapse;\">\n" +
                "                            <td width=\"560\" valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                              <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:12px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:18px;color:#666666;\">This daily newsletter was sent to info@name.com from company name because you subscribed. If you would not like to receive this email <a target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-size:12px;text-decoration:underline;color:#333333;\" class=\"unsubscribe\" href=\"\">unsubscribe here</a>.</p>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </table>\n" +
                "            <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\">\n" +
                "              <tr style=\"border-collapse:collapse;\">\n" +
                "                <td align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                  <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\">\n" +
                "                    <tr style=\"border-collapse:collapse;\">\n" +
                "                      <td align=\"left\" style=\"Margin:0;padding-left:20px;padding-right:20px;padding-top:30px;padding-bottom:30px;\">\n" +
                "                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                          <tr style=\"border-collapse:collapse;\">\n" +
                "                            <td width=\"560\" valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;\">\n" +
                "                              <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\n" +
                "                                <tr style=\"border-collapse:collapse;\">\n" +
                "                                  <td class=\"es-infoblock made_with\" align=\"center\" style=\"padding:0;Margin:0;line-height:14px;font-size:12px;color:#CCCCCC;\"><a target=\"_blank\" href=\"https://viewstripo.email/?utm_source=templates&utm_medium=email&utm_campaign=education&utm_content=trigger_newsletter2\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-size:12px;text-decoration:none;color:#CCCCCC;\"><img src=\"https://fkdgyh.stripocdn.email/content/guids/cab_pub_7cbbc409ec990f19c78c75bd1e06f215/images/78411525331495932.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;\" width=\"125\"></a></td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
        return emailContent;
    }
}