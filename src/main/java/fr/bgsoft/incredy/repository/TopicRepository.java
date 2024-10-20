package fr.bgsoft.incredy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.bgsoft.incredy.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {

}
