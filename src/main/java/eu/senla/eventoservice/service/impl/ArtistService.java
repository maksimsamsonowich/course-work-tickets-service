package eu.senla.eventoservice.service.impl;

import eu.senla.eventoservice.dto.*;
import eu.senla.eventoservice.exception.artist.ArtistNicknameMismatchException;
import eu.senla.eventoservice.exception.artist.ArtistNotFoundException;
import eu.senla.eventoservice.exception.other.CameNullEntityException;
import eu.senla.eventoservice.exception.user.UserNotFoundException;
import eu.senla.eventoservice.model.Artist;
import eu.senla.eventoservice.model.User;
import eu.senla.eventoservice.repository.ArtistRepository;
import eu.senla.eventoservice.repository.UserRepository;
import eu.senla.eventoservice.service.ArtistServiceInterface;
import eu.senla.eventoservice.util.mapper.MapperInterface;
import eu.senla.eventoservice.util.parser.ParserInterface;
import liquibase.pro.packaged.O;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArtistService implements ArtistServiceInterface {

    private final ParserInterface parser;

    private final UserRepository userRepository;

    private final ArtistRepository artistRepository;

    private final MapperInterface<ArtistModelDto, Artist> mapper;

    @Override
    public RequestStatusDto registerAnArtistCard(String email, ArtistModelDto artistModelDto) {

        Artist currentArtistModel = mapper.mapToEntity(artistModelDto, Artist.class);

        if (Objects.isNull(currentArtistModel))
            throw new CameNullEntityException("Came null entity");

        User currentUser = userRepository.getUserByCredential_Email(email)
                .orElseThrow(() -> new UserNotFoundException("There is no such user"));

        currentArtistModel.setCardOwner(currentUser);

        artistRepository.save(currentArtistModel);

        return new RequestStatusDto()
                .setStatusId(HttpStatus.OK.value())
                .setTimespan(Calendar.getInstance().getTime().toString())
                .setMessage("Artist \"" + currentArtistModel.getNickname() + "\" successfully register");
    }

    @Override
    public RequestStatusDto deleteAnArtistCard(DeleteArtistCardRequestDto deleteArtistCardRequest) {

        if (!deleteArtistCardRequest.getNickname().equals(deleteArtistCardRequest.getNicknameConfirmation()))
            throw new ArtistNicknameMismatchException("Artist nickname mismatch");

        Artist currentArtistModel = artistRepository.getArtistByNickname(deleteArtistCardRequest.getNickname())
                .orElseThrow(() -> new ArtistNotFoundException("There is no such artist card"));

        artistRepository.delete(currentArtistModel);

        return new RequestStatusDto()
                .setStatusId(HttpStatus.OK.value())
                .setTimespan(Calendar.getInstance().getTime().toString())
                .setMessage("Artist \"" + deleteArtistCardRequest.getNickname() + "\" successfully deleted");
    }

    @Override
    public RequestStatusDto changeArtistNickname(ChangeArtistNicknameRequestDto changeArtistNicknameRequest) {
        Artist currentArtistModel = artistRepository
                .getArtistByNickname(changeArtistNicknameRequest.getPreviousNickname())
                .orElseThrow(() -> new ArtistNotFoundException("There is no such artist card"));

        currentArtistModel.setNickname(changeArtistNicknameRequest.getNewNickname());

        artistRepository.save(currentArtistModel);

        return new RequestStatusDto()
                .setStatusId(HttpStatus.OK.value())
                .setTimespan(Calendar.getInstance().getTime().toString())
                .setMessage("Artist \"" + changeArtistNicknameRequest.getPreviousNickname() + "\" successfully " +
                        "change his nick name to \"" + changeArtistNicknameRequest.getNewNickname() + "\"");
    }

    @Override
    public ArtistModelDto getArtistByEventIdOrTitle(String getArtistByEventIdOrTitleDto) {
        Artist currentArtist;

        if (parser.isInputStringAnId(getArtistByEventIdOrTitleDto)) {
            currentArtist = artistRepository
                    .getArtistByEventId(Long.parseLong(getArtistByEventIdOrTitleDto))
                    .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));
        } else {
            currentArtist = artistRepository.getArtistByEventTitle(getArtistByEventIdOrTitleDto + "%")
                    .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));
        }

        return mapper.mapToDto(currentArtist, ArtistModelDto.class);
    }

    @Override
    public ArtistModelDto getArtistById(Long artistId) {
        Artist currentArtist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        ArtistModelDto artistModel = mapper.mapToDto(currentArtist, ArtistModelDto.class);

        for (EventModelDto eventModel : artistModel.getEvents()) {
            if (eventModel.getDescription().length() > 61)
                eventModel.setDescription(eventModel.getDescription().substring(0, 60) + "...");
            else
                eventModel.setDescription(eventModel.getDescription() + "...");
        }

        return artistModel;
    }

    @Override
    public List<ArtistModelDto> getTopThree() {
        List<Artist> artistList = artistRepository.getTopThree()
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        return mapper.listToDto(artistList, ArtistModelDto.class);
    }

    @Override
    public List<ArtistModelDto> getAllArtists() {
        List<Artist> artistsList =
                artistRepository.getAll()
                        .orElseThrow(() -> new ArtistNotFoundException("Artists not found"));

        return mapper.listToDto(artistsList, ArtistModelDto.class);

    }

}
