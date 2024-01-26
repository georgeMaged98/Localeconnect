export interface Itinerary {
  id: number,
  userId: number,
  name: string,
  description: string,
  numberOfDays: number,
  tags:Tag[],
  placesToVisit: string[],
  dailyActivities: string[],
  imageUrls:string[],
  expand:boolean
}

export enum Tag{
  ADVENTURE,
  BEACH,
  CULTURAL,
  FOODIE,
  HIKING,
  LUXURY,
  NATURE,
  ROAD_TRIP,
  URBAN_EXPLORATION,
  FAMILY_FRIENDLY,
  BACKPACKING,
  WELLNESS,
  WILDLIFE,
  HISTORICAL,
  ARTS_AND_CRAFTS,
  FESTIVALS,
  NIGHTLIFE,
  SHOPPING,
  WINTER_SPORTS,
  WATER_SPORTS
}
