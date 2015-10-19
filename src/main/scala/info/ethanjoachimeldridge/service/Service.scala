package info.ethanjoachimeldridge.service

import com.plameno.validation._

abstract class Service {
	def validateCallWith[A](validationFactory: A => FormValidator)(a: A) = {
		validationFactory(a).validate
	}
}