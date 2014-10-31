/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.AnimalPicture;

public interface AnimalPictureRepository extends JpaRepository<AnimalPicture, Long> {

}
