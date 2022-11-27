package com.imooc.restroom.service;

import com.imooc.restroom.api.IRestroomService;
import com.imooc.restroom.converter.ToiletConverter;
import com.imooc.restroom.dao.ToiletDao;
import com.imooc.restroom.entity.ToiletEntity;
import com.imooc.restroom.pojo.Toilet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RestController
@RequestMapping("toilet-service")
public class RestroomService implements IRestroomService {

    @Autowired
    private ToiletDao toiletDao;

    @Override
    @GetMapping("checkAvailable")
    public List<Toilet> getAvailableToilet() {
        List<ToiletEntity> result = toiletDao.findAllByCleanAndAvailable(true, true);
        return result.stream().map(ToiletConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping("occupy")
    public Toilet occupy(Long id) {
        ToiletEntity entity = toiletDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Toilet not found"));
        if (!entity.isAvailable() || !entity.isClean()) {
            throw new RuntimeException("Toilet not available or unclean");
        }

        entity.setAvailable(false);
        entity.setClean(false);
        toiletDao.save(entity);

        return ToiletConverter.convert(entity);
    }

    @Override
    @PostMapping("release")
    public Toilet release(Long id) {
        ToiletEntity entity = toiletDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Toilet not found"));
        entity.setAvailable(true);
        entity.setClean(true);
        toiletDao.save(entity);
        return ToiletConverter.convert(entity);
    }

    @Override
    @GetMapping("checkAvailability")
    public boolean checkAvailability(Long id) {
        ToiletEntity entity = toiletDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Toilet not found"));
        return entity.isAvailable();
    }
}
