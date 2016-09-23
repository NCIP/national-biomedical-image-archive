/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.basket;

import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.util.SeriesDTOConverter;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.Util;
import gov.nih.nci.nbia.searchresult.SeriesSearchResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ExternalDataBasket {

    //this is business logic - local only
    public List<SeriesSearchResult> getSeriesDTOList(List<String> patientIds,
                                                     List<String> studyInstanceUids,
                                                     List<String> seriesInstanceUids,
                                                     AuthorizationManager authManager) throws Exception
    {

        allSeriesDto = new ArrayList<SeriesDTO>();
        allSeriesIds = new ArrayList<String>();
        if (Util.hasAtLeastOneNonNullArgument(patientIds, studyInstanceUids, seriesInstanceUids))
        {
            try
            {
                getSeriesDTOFromPatient(patientIds, authManager);

            }catch(Exception ee)
            {
                handlerError(ee, "Cannot get series DTOs for given patients'list");
            }

            try
            {
                List<SeriesDTO> stList = generalSeriesDAO.findSeriesByStudyInstanceUid(studyInstanceUids,
                                                                                       authManager.getAuthorizedSites(),
                                                                                       authManager.getAuthorizedSeriesSecurityGroups());
                if (!Util.isEmptyCollection(stList))
                {
                    if (allSeriesDto.size() > 0)
                    {
                        appendSeriesDTOFromStudyIds(stList);
                    }else
                    {
                        setSeriesDTOFromStudyIds(stList);
                    }
                }
            }catch(Exception e3)
            {
                handlerError(e3, "Cannot get series DTOs for given studies'list");
            }

            try

            {
                List<SeriesDTO> ssList = generalSeriesDAO.findSeriesBySeriesInstanceUID(seriesInstanceUids,
                		                                                                authManager.getAuthorizedSites(),
                		                                                                authManager.getAuthorizedSeriesSecurityGroups());
                if(!Util.isEmptyCollection(ssList))
                {
                    setSeriesDTOFromSeriesIds(ssList);
                }
            }catch(Exception e4)
            {
                handlerError(e4, "Cannot get series DTOs for given series'list");
            }
        }
        return SeriesDTOConverter.convert(allSeriesDto);
    }

    //////////////////////////////////////////PRIVATE///////////////////////////////////////////
    private static Logger logger = Logger.getLogger(ExternalDataBasket.class);

    private GeneralSeriesDAO generalSeriesDAO = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
    private List<SeriesDTO> allSeriesDto;
    private List<String> allSeriesIds;


    private void handlerError(Exception e, String message) throws Exception
    {
        e.printStackTrace();
        logger.error(message);
        throw new Exception(message);
    }


    private void getSeriesDTOFromPatient(List<String> patientIds,
    		                             AuthorizationManager authManager)
    throws Exception
    {
        if (patientIds == null || patientIds.size() <= 0){
            return;
        }

        List<SeriesDTO> sList = generalSeriesDAO.findSeriesByPatientId(patientIds,
        		                                                       authManager.getAuthorizedSites(),
        		                                                       authManager.getAuthorizedSeriesSecurityGroups());

        if (sList != null)
        {
            for (int i = 0 ; i < sList.size(); i++)
            {
                allSeriesDto.add(sList.get(i));
                allSeriesIds.add(sList.get(i).getSeriesId());
            }
        }
    }

    private void appendSeriesDTOFromStudyIds(List<SeriesDTO> stList)
    {
        for (int i = 0; i < stList.size(); i++)
        {
            if(allSeriesIds.contains(stList.get(i).getSeriesId()) == false)
            {
                allSeriesDto.add(stList.get(i));
                allSeriesIds.add(stList.get(i).getSeriesId());
            }
        }
    }

    private void setSeriesDTOFromStudyIds(List<SeriesDTO> stList)
    {
        allSeriesDto = stList;
        for (int i = 0; i < stList.size(); i++)
        {
            allSeriesIds.add(stList.get(i).getSeriesId());
        }
    }




    private void setSeriesDTOFromSeriesIds(List<SeriesDTO> ssList)
    {
		for(int i = 0; i < ssList.size(); i++)
		{
			if (allSeriesIds.contains(ssList.get(i).getSeriesId()) == false)
			{
				allSeriesDto.add(ssList.get(i));
			}
        }
    }
}
