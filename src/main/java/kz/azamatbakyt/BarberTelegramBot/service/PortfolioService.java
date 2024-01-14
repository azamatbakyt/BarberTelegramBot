package kz.azamatbakyt.BarberTelegramBot.service;

import kz.azamatbakyt.BarberTelegramBot.entity.Portfolio;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private static final String BUCKET = "portfolio";

    private final PortfolioRepository portfolioRepository;
    private final S3Service s3Service;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, S3Service s3Service) {
        this.portfolioRepository = portfolioRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    public void save(MultipartFile file) throws IOException {
        byte[] data = file.getBytes();
        String fileName = file.getOriginalFilename();
        Portfolio portfolio = new Portfolio(BUCKET, fileName);
        portfolioRepository.save(portfolio);
        s3Service.persistObject(BUCKET, fileName, data);
    }

    public List<byte[]> findAll() {
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        return portfolioList.stream()
                .map(p -> s3Service.readObject(p.getBucket(), p.getKey()))
                .collect(Collectors.toList());
    }

    public List<Portfolio> getAllLinks(){
        return portfolioRepository.findAll();
    }

    @Transactional
    public void deleteImages(Long id){
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found"));
        portfolioRepository.delete(portfolio);
        s3Service.deleteObject(portfolio.getBucket(), portfolio.getKey());
    }

}
