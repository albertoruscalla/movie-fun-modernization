package org.superbiz.moviefun.movies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    private static final Logger logger = LoggerFactory.getLogger(MoviesController.class);

    private final MoviesRepository moviesRepository;

    public MoviesController(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @GetMapping
    @ResponseBody
    public List<Movie> find(@RequestParam(name = "field", required = false) String field,
                            @RequestParam(name = "key", required = false) String key,
                            @RequestParam(name = "start", required = false, defaultValue = "0") Integer start,
                            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (pageSize == null) {
            // no pagination
            if (field == null) {
                // no filter
                return moviesRepository.getMovies();
            } else {
                // filter
                return moviesRepository.findRange(field, key, 0, Integer.MAX_VALUE);
            }
        } else {
            // pagination
            if (field == null) {
                // no filter
                return moviesRepository.findAll(start, pageSize);
            } else {
                // filter
                return moviesRepository.findRange(field, key, start, pageSize);
            }
        }
    }

    @GetMapping("/count")
    @ResponseBody
    public int countByFieldAndKey(@RequestParam(name = "field", required = false) String field, @RequestParam(name = "key", required = false) String key) {
        if (field == null && key == null) {
            return moviesRepository.countAll();
        } else {
            return moviesRepository.count(field, key);
        }
    }

    @PostMapping
    public void add(@RequestBody Movie movie) {
        logger.debug("add movie: {}", movie);
        moviesRepository.addMovie(movie);
    }

    /**
     * DELETE /123
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        moviesRepository.deleteMovieId(id);
    }
}
