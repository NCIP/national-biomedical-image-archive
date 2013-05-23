/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

function FileData_Pairs(x)
{
x.t("lists","urls");
x.t("url","references");
x.t("products","dicom");
x.t("references","url");
x.t("references","section");
x.t("nbia","ftp");
x.t("cedara","nbia");
x.t("software","products");
x.t("section","lists");
x.t("client","software");
x.t("ftp","client");
x.t("dicom","cedara");
x.t("urls","associated");
x.t("associated","nbia");
}
