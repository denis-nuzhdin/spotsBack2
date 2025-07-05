package com.sportsocial.model

import jakarta.persistence.*

@Entity
@Table(name = "spots")
data class Spot(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    /** Название спота */
    @Column(nullable = false, length = 255)
    val name: String,
    /** Описание спота */
    @Column(nullable = false, length = 1000)
    val description: String,
    /** Город */
    @Column(nullable = false, length = 255)
    val city: String,
    /** Адрес */
    @Column(nullable = false, length = 255)
    val address: String,
    /** Цена входа */
    @Column(nullable = false)
    val price: Double,
    /** Расписание */
    @Column(nullable = false, length = 255)
    val schedule: String,
    /** Широта */
    @Column(nullable = false)
    val latitude: Double,
    /** Долгота */
    @Column(nullable = false)
    val longitude: Double,
    /** URL главного фото */
    @Column(length = 500)
    val mainPhotoUrl: String? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_type_id", nullable = false, foreignKey = ForeignKey(name = "fk_spots_spot_type"))
    val spotType: SpotType,
    
    @OneToMany(mappedBy = "spot", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val photos: List<SpotPhoto> = emptyList()
) 