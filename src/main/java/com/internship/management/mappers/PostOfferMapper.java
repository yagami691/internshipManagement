package com.internship.management.mappers;

import com.internship.management.dto.postOffer.ConventionRequestDto;
import com.internship.management.dto.postOffer.OfferRequestDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.entities.Convention;
import com.internship.management.entities.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PostOfferMapper {

    @Mapping(target = "convention", source = "file", qualifiedByName = "mapToConvention")
    Offer toEntity(OfferRequestDto dto);

    @Named("mapToConvention")
    static Convention mapToConvention(MultipartFile file) throws IOException {
        Convention c = new Convention();
        c.setPdfConvention(file.getBytes());
        return c;
    }

    @Mapping(target = "convention",  qualifiedByName = "conventionToDto")
    OfferResponseDto toDto(Offer offer);

    @Named("conventionToDto")
    default ConventionRequestDto mapConvention(Convention c) {

        if (c == null) {
            return null;
        }
        ConventionRequestDto dto = new ConventionRequestDto();
        dto.setFile(c.getPdfConvention());
        return dto;
    }

    List<OfferResponseDto> toDtoList(List<Offer> offers);
}
