#pragma once

#include <Kismet/BlueprintFunctionLibrary.h>
#include <Runtime/Launch/Resources/Version.h>
#include <Async/Async.h>
#include <Engine.h>

#include "NativeUI/Enums/ToastLengthMessage.h"

#include "MobileNativeCodeBlueprint.generated.h"


// #~~~~~~~~~~~~~~~~~~~~~~~~~ begin 2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//-- Dispatcher
DECLARE_DYNAMIC_DELEGATE_OneParam(FTypeDispacth, const FString&, ReturnValue); // DispatchName, ParamType, ParamName  



//~~~~~~~~~~~~~~~~~~~~~~~~~~~ end 2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


UCLASS()
class MOBILENATIVECODE_API UMobileNativeCodeBlueprint : public UBlueprintFunctionLibrary
{
  GENERATED_BODY()

public:
  UMobileNativeCodeBlueprint(const FObjectInitializer& ObjectInitializer) : Super(ObjectInitializer) {};

  // #~~~~~~~~~~~~~~~~~~~~~~~~ begin 2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //-- Dispatcher
  
	static FTypeDispacth StaticValueDispatch;
	static void StaticFunctDispatch(const FString& ReturnValue);

#if PLATFORM_IOS
  static void CallBackCppIOS(NSString* sResult);
#endif //PLATFORM_IOS
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~ end 2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


  /**
   * Concatenation of the platform name from native code
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static FString HelloWorld(FString MyStr = "Hello World");

  /**
   * Asynchronous platform name concatenation from native code
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static void asyncHelloWorld(const FTypeDispacth& CallBackPlatform, FString MyStr = "async Hello World");

  /**
   * Displaying a pop-up message
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static void ShowToastMobile(FString Message, EToastLengthMessage Length);

  /**
   * Example of passing different types of arrays and returning a String array with two values
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static void ExampleArray(FString& Arr1, FString& Arr2);

  /**
   * Returns information about the device
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static FString GetDeviceInfo();

  /**
   * Only for Android. Example of working with Java objects inside C++
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static void ExampleMyJavaObject(FString& JavaBundle);



  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void OpenGallery();

  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void OpenGallery_CallBack_Size(int return_size);

  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void DispatchGetter(const FTypeDispacth& CallBackPlatform);


  /*
   * x25519 ��ũ��Ű�� �����մϴ�. 
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void CreateSecretKey();

	/*
	 * �ۺ� Ű�� �����մϴ�. ���� ��ũ��Ű�� ������ ���Ŀ��� �մϴ�.
	 */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void CreatePublicKey();

	/*
	 * Shared Key�� �����մϴ�. �̹� ��ũ��Ű�� ������ �����̰�, ���Ҿ� ������ �ۺ�Ű�� ���޹��� �Ŀ� �����Ͽ��� �մϴ�.
	 */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void CreateSharedKey();


  /*
���޹��� ����� ��ȣŰ�� Byte Array�� �����Ͻʽÿ�.
�̶�, �̸��� �ݵ�� OpponentKey ���� �մϴ�. asyncAndroidFunctionLibClass.java - CreateSharedcKey ����.
 ������ ��δ� �ش� ������Ʈ�� ĳ�� ����
*/
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void SaveOpponentKey(TArray<uint8> OpponentKey);

  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void  Vibrate_During_Duration(int duration);

  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void  Vibrate_Keep_Going();

  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void  Vibrate_Cancel();
};
