<%--L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L--%>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
                      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
  <title>Data Basket Download</title>
</head>

<body>
  <h2>Data Basket Download</h2>
  
  <div style="margin-top: 20px">  
    <span>Please do not close this window until the download is complete.</span>
    <!--  i dont think it really matters if the window is open -->
  </div>  
  
  <form id="autoDownloadForm"
        action="servlet/BasketDownloadServlet"
        method="get"/>

  <script type="text/javascript">
    window.document.getElementById('autoDownloadForm').submit();
    //would be nice to close the window automatically... but cant
    //because IE7 info bar is on this window and user needs to click Download file
    //with 'automatic prompt for donwnload' set false
  </script>
</body>

</html>