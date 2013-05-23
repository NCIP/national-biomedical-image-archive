/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

PageBot.prototype.namespaceResolver = 
function(prefix)
{
  if (prefix == 'html' ||
      prefix == 'xhtml' ||
      prefix == 'x')
  {
    return 'http://www.w3.org/1999/xhtml';
  }
  else if (prefix == 'mathml')
  {
    return 'http://www.w3.org/1998/Math/MathML'
  }
  else
  {
    throw new Error("Unknown namespace: " + prefix + ".")
  }
}

PageBot.prototype.findElementUsingFullXPath = 
function(xpath, inDocument) {
    if (browserVersion.isIE && !inDocument.evaluate) {
        addXPathSupport(inDocument);
    }

    // HUGE hack - remove namespace from xpath for IE
    if (browserVersion.isIE)
        xpath = xpath.replace(/x:/g,'')

    // Use document.evaluate() if it's available
    if (inDocument.evaluate) {
        // cfis
        //return inDocument.evaluate(xpath, 
              inDocument, null, 0, null).iterateNext();
        return inDocument.evaluate(xpath,
          inDocument, this.namespaceResolver, 0, null).iterateNext();
    }

    // If not, fall back to slower JavaScript implementation
    var context = new XPathContext();
    context.expressionContextNode = inDocument;
    var xpathResult = new XPathParser().parse(xpath).evaluate(context);
    if (xpathResult && xpathResult.toArray) {
        return xpathResult.toArray()[0];
    }
    return null;
};