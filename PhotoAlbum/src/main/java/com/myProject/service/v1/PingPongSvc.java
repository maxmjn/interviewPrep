package com.myProject.service.v1;

import com.myProject.model.PingPongModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PingPongSvc {

  private static final Logger LOGGER = LoggerFactory.getLogger(PingPongSvc.class);

  public PingPongModel pingPong(String value){

    LOGGER.info(PingPongSvc.class.getSimpleName());
    PingPongModel pingPongModel = new PingPongModel();

    if(StringUtils.isNotEmpty(value) && value.equalsIgnoreCase("ping")){
      pingPongModel.setPing(value);
      pingPongModel.setPong("pong");
    }else {
      pingPongModel.setError("Error: value not ping");
    }

    return pingPongModel;
  }

}
