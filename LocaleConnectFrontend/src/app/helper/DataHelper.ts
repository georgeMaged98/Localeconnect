export function dataToList(data: string): string[]{
  return  data
    .split(',')
    .map((place: string) => place.trim())
    .filter((place: string) => place.length > 0);
}
