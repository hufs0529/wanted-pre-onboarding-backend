package wanted.wantedpreonboardingbackend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.wantedpreonboardingbackend.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
